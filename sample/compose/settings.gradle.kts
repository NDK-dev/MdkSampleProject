import java.util.Properties

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
        val localProperties = Properties().apply {
            val f = file("local.properties")
            if (f.exists()) {
                load(f.inputStream())
            }
        }
        maven(localProperties.getProperty("maven.messay"))
    }
}

rootProject.name = "MdkSampleProject"
include(":app")
