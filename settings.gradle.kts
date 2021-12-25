rootProject.name = "fusion-alpha-3"

include("fusion:fusion-eureka")
include("fusion:fusion-mu")
include("fusion:fusion-plugins")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
