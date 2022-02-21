plugins {
    `maven-publish`
    `kotlin-dsl`
    id("org.jetbrains.dokka") version "1.4.32"
    maven
    signing
}

dependencies {
    implementation(gradleApi())
    compileOnly("com.android.tools.build:gradle:4.1.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

