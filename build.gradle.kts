import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project
val junitVersion: String by project
val assertjVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project

val koinVersion: String by project
val kediatrVersion: String by project
val valikatorVersion: String by project

val kubernetesVersion: String by project

plugins {
    kotlin("jvm") version "1.4.0"
    application
}

repositories {
    jcenter()
}

group = "cozy"
version = "0.0.1"

application {
    mainClass.set("cozy.ApplicationKt")
}

dependencies {
    // Main dependencies including Ktor and the Kotlin language
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Libraries used in the Ktor application
    implementation("org.koin:koin-ktor:$koinVersion")
    implementation("com.trendyol:kediatr-core:$kediatrVersion")
    implementation("org.valiktor:valiktor-core:$valikatorVersion")
    implementation("io.fabric8:kubernetes-client:$kubernetesVersion")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.66")

    // Main dependencies for testing including Ktor and the JUnit 5 testing framework
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.koin:koin-test-junit5:$koinVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")
