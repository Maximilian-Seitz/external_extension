plugins {
    kotlin("jvm") version "1.3.72"
    kotlin("kapt") version "1.3.72"
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":annotation"))
    kapt(project(":annotation"))

    testImplementation("junit:junit:4.12")
}
