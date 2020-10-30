val openjdkVersion = "15"
val gradleVersion = "latest"

job("Build & test project") {
    startOn {
        gitPush {
            enabled = false
        }

        codeReviewOpened()
    }

    container("gradle:$gradleVersion") {
        shellScript {
            content = "gradle build"
        }
    }
}
