import ch.acanda.gradle.fabrikt.ValidationLibraryOption

val koin_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometheus_version: String by project
val koin_annotations_version: String by project
val exposed_version: String by project

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.ktor.plugin") version "3.0.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
    id("ch.acanda.gradle.fabrikt") version "1.9.0"
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

group = "com.supcis.kaij.hackday_building_management"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

fabrikt {
    generate("facility") {
        apiFile = file("src/main/resources/openapi/facility.yaml")
        basePackage = "com.supcis.kaij.hackday_building_management"
        validationLibrary = NoValidation
        controller {
            generate = enabled
            authentication = disabled
            suspendModifier = enabled
            completionStage = disabled
            target = Ktor
        }
        model {
            generate = enabled
            extensibleEnums = disabled
            javaSerialization = disabled
            quarkusReflection = disabled
            micronautIntrospection = disabled
            micronautReflection = disabled
            includeCompanionObject = enabled
            sealedInterfacesForOneOf = enabled
            ignoreUnknownProperties = disabled
            serializationLibrary = Kotlin
        }    }
}

dependencies {
    implementation("io.ktor:ktor-server-compression")
    implementation("io.ktor:ktor-server-default-headers")
    implementation("io.ktor:ktor-server-forwarded-header")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-call-id")
    implementation("io.ktor:ktor-server-metrics-micrometer")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometheus_version")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("io.insert-koin:koin-annotations:$koin_annotations_version")
    ksp("io.insert-koin:koin-ksp-compiler:$koin_annotations_version")
    implementation("io.github.flaxoos:ktor-server-rate-limiting:2.1.2")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.nats:jnats:2.11.0")
    implementation("io.arrow-kt:arrow-core:2.0.0")

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
