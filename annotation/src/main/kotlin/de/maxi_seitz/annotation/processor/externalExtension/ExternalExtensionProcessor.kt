package de.maxi_seitz.annotation.processor.externalExtension

import com.squareup.kotlinpoet.*
import de.maxi_seitz.annotation.ExternalExtension
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.tools.Diagnostic.Kind.ERROR


@SupportedSourceVersion(SourceVersion.RELEASE_12)
@SupportedAnnotationTypes("de.maxi_seitz.annotation.ExternalExtension")
@SupportedOptions(ExternalExtensionProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class ExternalExtensionProcessor : AbstractProcessor() {
	
	private lateinit var processingEnvironment: ProcessingEnvironment
	private lateinit var elementUtils: Elements
	
	companion object {
		const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
		
		const val EXTENSION_FUNCTIONS_ONLY_ERROR = "@ExternalExtension must be used for extension functions only."
	}
	
	private val kaptKotlinGeneratedDir: String
		get() = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: err("Can't find the target directory for generated Kotlin files.")
	
	@Synchronized
	override fun init(processingEnvironment: ProcessingEnvironment) {
		super.init(processingEnvironment)
		this.processingEnvironment = processingEnvironment
		this.elementUtils = processingEnvironment.elementUtils
	}
	
	override fun process(
			annotations: MutableSet<out TypeElement>?,
			roundEnv: RoundEnvironment
	): Boolean {
		try {
			//Get annotated elements
			val annotatedElements = roundEnv.getElementsAnnotatedWith(ExternalExtension::class.java)
			if (annotatedElements.isEmpty()) return false
			
			//Get functions and their annotations
			val annotationTargets = annotatedElements.map {
				Pair(
						it.getAnnotation(ExternalExtension::class.java),
						it as? ExecutableElement ?: err(EXTENSION_FUNCTIONS_ONLY_ERROR)
				)
			}
			
			//Get target directory for code generation
			val targetDir = File(kaptKotlinGeneratedDir)
			
			generateExtensionParents(annotationTargets, targetDir)
			
			return true
		} catch (error: AnnotationProcessorException) {
			processingEnv.messager.printMessage(ERROR, error.message)
			return false
		}
	}
	
	@Throws(AnnotationProcessorException::class)
	private fun generateExtensionParents(
			annotationTargets: List<AnnotatedFunction<ExternalExtension>>,
			targetDir: File
	) {
		val packages = ExternalExtensionPackages()
		
		for ((externalExtension, annotatedFunction) in annotationTargets) {
			val targetReceiver = @Suppress("DEPRECATION") externalExtension.parentType.asTypeName()
			val targetPackage = annotatedFunction.packageName
			val functionName = annotatedFunction.simpleName.toString()
			val originalReceiver = annotatedFunction.extensionReceiver ?: err(EXTENSION_FUNCTIONS_ONLY_ERROR)
			val parameters = annotatedFunction.extensionFunctionParameters
			
			val parameterNames = parameters.map {
				it.simpleName.toString()
			}
			
			val parameterTypes = parameters.map {
				@Suppress("DEPRECATION") it.asType().asTypeName()
			}
			
			packages[targetPackage][functionName][targetReceiver, parameterNames, parameterTypes] += originalReceiver
		}
		
		val generatedFiles = mutableListOf<FileSpec>()
		
		packages.forEachFunctionName { targetPackage, functionName, functions ->
			val file = FileSpec.builder(targetPackage, "GENERATED_$functionName").apply {
				functions.forEachReceiver { targetReceiver, parameterNames, parameterTypes, originalReceivers ->
					val originalInvocation = "this.$functionName(${parameterNames.joinToString(", ")})"
					
					addFunction(
							FunSpec.builder(functionName).apply {
								addAnnotation(Generated::class)
								
								receiver(targetReceiver)
								
								addParameters(parameterTypes.mapIndexed { i, parameterType ->
									val parameterName = parameterNames.getOrElse(i) { "arg$i" }
									
									ParameterSpec(parameterName, parameterType)
								})
								
								addStatement("""
									|return when(this) {
									|	${originalReceivers.joinToString("\n|\t") { originalReceiver ->
											"is $originalReceiver -> $originalInvocation"
										}}
									|	else -> throw NotImplementedError("Extension method implemented for ${"$"}{this::class}.")
									|}
									""".trimMargin()
								)
							}.build()
					)
				}
			}.build()
			
			generatedFiles.add(file)
		}
		
		targetDir.mkdirs()
		
		generatedFiles.forEach {
			it.writeTo(targetDir)
		}
	}
	
	private val Element.packageName: String
		get() = elementUtils.getPackageOf(this).qualifiedName.toString()
	
	private val ExecutableElement.extensionReceiver: TypeMirror?
		get() = if (isExtensionFunction) parameters.firstOrNull()?.asType() else null
	
	private val ExecutableElement.extensionFunctionParameters: List<VariableElement>
		get() = if (isExtensionFunction) parameters.filterIndexed { i, _ -> i > 0 } else listOf()
	
	private val ExecutableElement.isExtensionFunction: Boolean
			get() = "\$this\$$simpleName" == parameters.firstOrNull()?.simpleName?.toString()
	
	private val ExternalExtension.parentType: TypeMirror
		get() = try {
				err("Error expected, but call succeeded, getting: ${
					target.qualifiedName.toString()
				}")
			} catch (e: MirroredTypeException) {
				e.typeMirror
			}
}