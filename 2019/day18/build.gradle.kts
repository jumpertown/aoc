import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm") version "1.3.61"
    application
}
group = "org.example"
version = "1.0-SNAPSHOT"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jgrapht:jgrapht-core:1.3.0")
    implementation(kotlin("script-runtime"))
}
repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

application {
    mainClassName = "main.MainKt"
    applicationDefaultJvmArgs = listOf("-Xms4096m", "-Xmx8192m")
}