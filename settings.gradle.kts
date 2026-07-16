rootProject.name = "StockAppQuotes"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val localProperties = java.util.Properties().apply {
    val f = file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
val useLocalDesignSystem = localProperties.getProperty("useLocalDesignSystem", "false").toBoolean()

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        mavenLocal()
    }
}

include(":quotes")
include(":sample")
include(":sample-android")

if(useLocalDesignSystem){
    includeBuild("../stockapp-designsystem"){
        dependencySubstitution {
            substitute(module("com.danilobarreto.stockapp:designsystem"))
                .using(project(":designsystem"))
        }
    }
}