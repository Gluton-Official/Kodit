import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "dev.gluton"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    val kordVersion: String by project
    val slf4jVersion: String by project
    val exposedVersion: String by project
    val mysqlVersion: String by project
    val dotenvVersion: String by project

    implementation("dev.kord:kord-core:$kordVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("mysql:mysql-connector-java:$mysqlVersion")
    implementation("io.github.cdimascio:dotenv-kotlin:$dotenvVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.apply {
        jvmTarget = "17"
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

application {
    mainClass.set("MainKt")
}
