val openjdkVersion = "15"
val gradleVersion = "latest"

var currentBranch: String? = null

job("Initialize automation") {
    container("openjdk:$openjdkVersion") {
        kotlinScript { api ->
            currentBranch = api.gitBranch()
        }
    }
}

job("Build & test project") {
    startOn {
        gitPush {
            val isCurrentBranchMaster = currentBranch == "master"
            enabled = isCurrentBranchMaster
        }

        codeReviewOpened()
        codeReviewClosed()
    }

    container("gradle:$gradleVersion") {
        shellScript {
            content = "gradle build"
        }
    }
}
