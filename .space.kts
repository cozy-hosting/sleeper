val openjdkVersion = "15"

job("Build & test project") {
    parallel {
        gradlew("openjdk:$openjdkVersion", "assemble")
        gradlew("openjdk:$openjdkVersion", "test")
    }
}
