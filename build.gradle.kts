plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.sonarqube)
}

val localProperties = java.util.Properties().apply {
    val f = file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
val usingLocalComposite = listOf("useLocalDesignSystem", "useLocalAuth")
    .any { localProperties.getProperty(it, "false").toBoolean() }

sonar {
    properties {
        property("sonar.projectKey", "dgbarreto_stockapp-quotes")
        property("sonar.organization", "dgbarreto")
    }
}

// Demo/fixture modules, not product code — excluded from analysis.
project(":sample") {
    sonar {
        isSkipProject = true
    }
}
project(":sample-android") {
    sonar {
        isSkipProject = true
    }
}

allprojects {
    // Gradle's dependency locking is incompatible with composite-build substitution
    // (gradle/gradle#4749, #28856): resolving a locked configuration while
    // useLocalDesignSystem/useLocalAuth substitutes a module via includeBuild() throws
    // UnsupportedOperationException in DefaultDependencyLockingProvider. Locking a
    // pinned artifact version is meaningless anyway when depending on a local,
    // unpublished module build, so skip it in that mode (same fix as stockapp-auth's
    // useLocalDesignSystem, see its build.gradle.kts).
    if (!usingLocalComposite && name != "sample" && name != "sample-android") {
        dependencyLocking {
            lockAllConfigurations()
        }
    }
}
