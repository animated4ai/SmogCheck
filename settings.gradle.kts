pluginManagement {
    repositories {
        // Local pre-fetched Maven repo (built with toolchain/mvnfetch.py).
        // Used because the sandbox egress proxy breaks Gradle's HTTP client;
        // curl works, so all artifacts were fetched with curl in advance.
        maven { url = uri("file:///home/hatch/workspace/toolchain/local-maven-repo") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("file:///home/hatch/workspace/toolchain/local-maven-repo") }
        google()
        mavenCentral()
    }
}
rootProject.name = "SmogCheck"
include(":app")
