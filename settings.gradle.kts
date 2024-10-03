import java.util.Properties

rootProject.name = "MdkProject"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val localProperties = Properties().apply {
    val f = file("local.properties")
    if (f.exists()) {
        load(f.inputStream())
    }
}

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
        maven(extra["maven.messay"] as String) {
            credentials {
                username = localProperties.getProperty("maven.messay.username")
                password = localProperties.getProperty("maven.messay.password")
            }
        }
    }
}

include(":composeApp")