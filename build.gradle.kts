import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    application
}

group = "dev.cbyrne.adieux"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("xyz.gianlu.librespot:librespot-player:1.6.1:thin")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}