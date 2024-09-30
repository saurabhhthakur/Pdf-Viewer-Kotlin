pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral() // Place this first
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "PDF Viewer"
include(":app")
