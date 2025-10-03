import java.util.Properties

val localProperties = Properties().apply {
    val f = file("local.properties")
    if (f.exists()) {
        load(f.inputStream())
    }
}

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(extra["maven.messay"] as String) {
            credentials {
                username = localProperties.getProperty("maven.messay.username")
                password = localProperties.getProperty("maven.messay.password")
            }
        }
    }
}

rootProject.name = "MdkSampleProject"
include(":app")
