plugins {
    `maven-publish`
    `kotlin-dsl`
    id("org.jetbrains.dokka") version "1.4.32"
    maven
    signing
}

dependencies {
    implementation(gradleApi())
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

