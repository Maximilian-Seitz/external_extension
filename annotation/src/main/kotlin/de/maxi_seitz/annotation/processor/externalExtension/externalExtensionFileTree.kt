package de.maxi_seitz.annotation.processor.externalExtension

import com.squareup.kotlinpoet.TypeName
import javax.lang.model.type.TypeMirror

class ExternalExtensionPackages {
	private val packages: MutableMap<String, ExternalExtensionPackage> = mutableMapOf()
	
	operator fun get(packageName: String) : ExternalExtensionPackage {
		return packages.getOrPut(packageName) {
			ExternalExtensionPackage()
		}
	}
	
	fun forEachFunctionName(callback: (packageName: String, functionName: String, functions: ExternalExtensionFunctions) -> Unit) {
		packages.forEach { (packageName, externalExtensionPackage) ->
			externalExtensionPackage.functionNames.forEach { (functionName, functions) ->
				callback(packageName, functionName, functions)
			}
		}
	}
}

class ExternalExtensionPackage {
	internal val functionNames: MutableMap<String, ExternalExtensionFunctions> = mutableMapOf()
	
	operator fun get(functionName: String) : ExternalExtensionFunctions {
		return functionNames.getOrPut(functionName) {
			ExternalExtensionFunctions()
		}
	}
}

class ExternalExtensionFunctions {
	private val receivers: MutableMap<ExternalExtensionFunctionSignature, ExternalExtensionFunction> = mutableMapOf()
	
	operator fun get(
			receiver: TypeName,
			parameterNames: List<String>,
			parameterTypes: List<TypeName>
	) : ExternalExtensionFunction {
		return receivers.getOrPut(Pair(receiver, parameterTypes)) {
			ExternalExtensionFunction(parameterNames)
		}.also {
			if (it.parameterNames != parameterNames) {
				it.useNames = false
			}
		}
	}
	
	fun forEachReceiver(callback: (receiver: TypeName, parameterNames: List<String>, parameterTypes: List<TypeName>, childFunctions: Map<TypeMirror, String>) -> Unit) {
		receivers.forEach { (signature, function) ->
			callback(signature.receiver, function.parameterNames, signature.parameters, function.childFunctions.toMap())
		}
	}
}

private typealias ExternalExtensionFunctionSignature = Pair<TypeName, List<TypeName>>
val ExternalExtensionFunctionSignature.receiver get() = first
val ExternalExtensionFunctionSignature.parameters get() = second

class ExternalExtensionFunction(
		parameterNames: List<String>
) {
	internal var childFunctions: MutableMap<TypeMirror, String> = mutableMapOf()
	internal var useNames = true
	
	internal val parameterNames = parameterNames
		get() = if (useNames) field else emptyList()
	
	operator fun set(receiver: TypeMirror, functionName: String) {
		childFunctions[receiver] = functionName
	}
}
