import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.jfrog.artifactory") version "4.24.21"
    id("org.springframework.boot") version "2.5.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "com.kaizensundays"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
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
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.apache.logging.log4j:log4j-api:2.17.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")
    implementation("org.apache.logging.log4j:log4j-jcl:2.17.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.3")

    implementation("org.apache.ignite:ignite-core:2.11.0")
    implementation("org.apache.ignite:ignite-slf4j:2.11.0")
    implementation("org.apache.ignite:ignite-spring:2.11.0")
    implementation("com.h2database:h2:1.4.197")

    implementation("io.projectreactor.tools:blockhound:1.0.6.RELEASE")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    implementation("org.postgresql:postgresql:9.4.1212")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
    useJUnit()
    filter {
        excludeTestsMatching("*RemoteTest")
    }
}

tasks.bootJar {
    archiveFileName.set("service.jar")
    destinationDirectory.set(file("bin"))
}
