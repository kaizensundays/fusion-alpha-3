import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.jfrog.artifactory") version "4.24.21"
    id("org.springframework.boot") version "2.5.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    //id("com.kaizensundays.plugin-one")
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "com.kaizensundays"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    //mavenCentral()
    maven {
        url = uri(project.properties["artifactory.url"]!!)
        isAllowInsecureProtocol = true
        credentials {
            username = project.properties["artifactory.username"] as String
            password = project.properties["artifactory.password"] as String
        }
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2020.0.4")
    }
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.apache.logging.log4j:log4j-api:2.17.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")
    implementation("org.apache.logging.log4j:log4j-jcl:2.17.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.0")

    implementation("io.projectreactor.tools:blockhound:1.0.6.RELEASE")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    filter {
        excludeTestsMatching("*RemoteTest")
    }
}

tasks.bootJar {
    archiveFileName.set("service.jar")
    destinationDirectory.set(file("bin"))
}


class GojoTwo : Plugin<Project> {
    override fun apply(prj: Project) {
        prj.tasks.register("oops", object : Action<Task> {
            override fun execute(t: Task) {
                t.doLast {
                    println("Hello from plugin 'GojoTwo'")
                }
            }
        })

    }
}