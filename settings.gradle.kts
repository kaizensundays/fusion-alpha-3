rootProject.name = "fusion-alpha-3"

include("fusion:fusion-eureka")
include("fusion:fusion-mu")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
