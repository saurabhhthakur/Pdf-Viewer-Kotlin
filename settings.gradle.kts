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
       // maven { url ("http://jcenter.bintray.com") }
        jcenter()
        mavenCentral()
    }
}

rootProject.name = "PDF Viewer"
include(":app")
