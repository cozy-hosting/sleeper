job("Build Docker") {

    startOn {
        gitPush {
            branchFilter {
                +"refs/heads/main"
            }
        }
    }
    docker {
        build {
            file = "./Dockerfile"
        }
    }

}
