val openjdkVersion = "15"
val gradleVersion = "6.7-jdk11"

job("Build & test project") {
    container("gradle:$gradleVersion") {
        kotlinScript { api ->
            val isBranchMaster = api.gitBranch().contains("master")

            if (isBranchMaster) {
               shellScript {
                   content = "gradle build"
               }
            }
        }
    }
}
