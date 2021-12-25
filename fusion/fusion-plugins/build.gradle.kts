import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.gradle.java-gradle-plugin")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.gradle.maven-publish")
    //id("com.kaizensundays.fusion-plugins")
}

group = "com.kaizensundays"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    mavenLocal()
}

val springVersion = "5.3.12"
val jacksonVersion = "2.10.3"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework:spring-core:$springVersion")

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")

    implementation("io.fabric8:kubernetes-client:4.10.1")
    implementation("com.fkorotkov:kubernetes-dsl:2.8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

gradlePlugin {
    val oops by plugins.creating {
        id = "com.kaizensundays.fusion-plugins"
        implementationClass = "com.kaizensundays.fusion.plugin.one.DeployGojo"
    }
}

class GojoTwo : Plugin<Project> {
    override fun apply(prj: Project) {
        prj.tasks.register("two", object : Action<Task> {
            override fun execute(t: Task) {
                t.doLast {
                    println("Hello from plugin 'GojoTwo'")
                }
            }
        })

    }
}

apply<GojoTwo>()