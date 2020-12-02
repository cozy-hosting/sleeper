package cozy.services.cert.jobs

import io.fabric8.kubernetes.api.model.batch.Job
import io.fabric8.kubernetes.api.model.batch.JobBuilder

data class ApprovalJob(
    val name: String,
    val signingRequestName: String
) {

    val job: Job = JobBuilder()
        .withNewMetadata().withName(name).endMetadata()
        .withNewSpec()
        .withNewTemplate()
        .withNewSpec()
        .addNewContainer()
        .withName("kubectl")
        .withImage("bitnami/kubectl:latest")
        .withArgs("certificate", "approve", signingRequestName)
        .endContainer()
        .withRestartPolicy("Never")
        .endSpec()
        .endTemplate()
        .endSpec()
        .build()

}
