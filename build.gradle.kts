plugins {
    java
    id("io.quarkus")
    id("com.diffplug.spotless") version "6.25.0"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    // BOM
    implementation(platform("io.cloudevents:cloudevents-bom:2.5.0"))
    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))

    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-oidc")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-redis-client")
    implementation("io.quarkus:quarkus-smallrye-fault-tolerance")
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka")

    // Cloud Events
    implementation("io.cloudevents:cloudevents-core")
    implementation("io.cloudevents:cloudevents-api")
    implementation("io.cloudevents:cloudevents-json-jackson")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "dev.fernyettheplant"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

spotless {
    java {
        removeUnusedImports()
        importOrder()
        palantirJavaFormat()
        formatAnnotations()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
    gherkin {
        target("src/**/*.feature")
        gherkinUtils()
    }
    yaml {
        target("src/**/*.yaml", ".github/**/*.yaml")
        jackson()
    }
}
