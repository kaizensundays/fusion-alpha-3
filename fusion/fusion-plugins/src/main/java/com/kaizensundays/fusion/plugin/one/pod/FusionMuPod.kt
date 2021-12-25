package com.kaizensundays.fusion.plugin.one.pod

import com.fkorotkov.kubernetes.*
import io.fabric8.kubernetes.api.model.Pod

/**
 * Created: Sunday 12/5/2021, 1:23 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class FusionMuPod(val podName: String = "fusion-mu") : Pod() {

    init {
        metadata {
            this.name = podName
            namespace = "default"
            labels = mapOf("tag" to podName)
        }
        spec {
            containers = listOf(
                newContainer {
                    name = podName
                    image = "localhost:32000/fusion-mu:latest"
                    imagePullPolicy = "Always"
                    lifecycle {
                        preStop {
                            exec {
                                command = listOf("/bin/sh", "-c", "PID=`pidof java` && kill -SIGTERM \$PID && while pidof java > /dev/null; do sleep 3; done;")
                            }
                        }
                    }
                }
            )
            restartPolicy = "Never"
            terminationGracePeriodSeconds = 100
        }
    }

}