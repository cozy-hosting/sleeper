val kotlinVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project

val koinVersion: String by project
val kediatrVersion: String by project
val valikatorVersion: String by project

val kubernetesVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.4.0"
}

group = "cozy"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("org.koin:koin-ktor:$koinVersion")
    implementation("com.trendyol:kediatr-core:$kediatrVersion")
    implementation("org.valiktor:valiktor-core:$valikatorVersion")

    implementation("io.kubernetes:client-java:$kubernetesVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")
