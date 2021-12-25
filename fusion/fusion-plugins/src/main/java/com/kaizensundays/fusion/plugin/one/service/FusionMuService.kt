package com.kaizensundays.fusion.plugin.one.service

import com.fkorotkov.kubernetes.metadata
import com.fkorotkov.kubernetes.newServicePort
import com.fkorotkov.kubernetes.spec
import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.api.model.Service

/**
 * Created: Sunday 12/5/2021, 1:28 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class FusionMuService(private val serviceName: String = "fusion-mu") : Service() {

    constructor(pod: Pod) : this(pod.metadata.name)

    init {
        metadata {
            name = serviceName
        }
        spec {
            selector = mapOf("tag" to serviceName)
            ports = listOf(
                newServicePort() {
                    name = "http"
                    port = 7701
                    nodePort = 31771
                }
            )
            type = "NodePort"
        }
    }

}