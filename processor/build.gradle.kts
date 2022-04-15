
val kotlinVersion = "1.5.31"
val pAnnotation = project(":annotation")

plugins {
    kotlin("jvm") version "1.5.31"
    // Apply the java-library plugin for API and implementation separation.
    java
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(kotlin("stdlib"))

    implementation("com.google.devtools.ksp:symbol-processing-api:$kotlinVersion-1.0.0")
    implementation("com.squareup:kotlinpoet:1.10.2")
    implementation(pAnnotation)

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    testImplementation(pAnnotation)
}
