plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.sf.jung:jung-graph-impl:2.1.1")
    implementation("net.sf.jung:jung-visualization:2.1.1")
    implementation("net.sf.jung:jung-algorithms:2.1.1")


    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.20-RC")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}