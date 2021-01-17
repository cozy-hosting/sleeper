package cozy.identity.jobs

import cozy.identity.data.SigningRequest
import cozy.repositories.jobs.data.AbstractJob
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.batch.Job
import io.fabric8.kubernetes.api.model.batch.JobBuilder

data class ApprovalJob(
    override val name: String,
    val namespace: Namespace,
    val signingRequest: SigningRequest
): AbstractJob(name) {

    override val job: Job = JobBuilder()
        .withNewMetadata()
        .withName(name)
        .withNamespace(namespace.metadata.name)
        .endMetadata()
        .withNewSpec()
        .withNewTemplate()
        .withNewSpec()
        .withServiceAccount("sleeper")
        .addNewContainer()
        .withName("kubectl")
        .withImage("bitnami/kubectl:latest")
        .withArgs("certificate", "approve", signingRequest.name)
        .endContainer()
        .withRestartPolicy("Never")
        .endSpec()
        .endTemplate()
        .endSpec()
        .build()

}
