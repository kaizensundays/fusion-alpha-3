package com.kaizensundays.fusion.plugin.one

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.kaizensundays.fusion.plugin.one.pod.FusionMuPod
import com.kaizensundays.fusion.plugin.one.service.FusionMuService
import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import java.lang.Thread.sleep
import java.net.InetAddress
import java.util.*

/**
 * Created: Friday 12/3/2021, 1:42 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DeployGojo : Plugin<Project> {

    private val mapper = ObjectMapper(YAMLFactory())

    private val namespace = "default"

    private val location = "deploy.yaml"

    private val deleteWaitDelayMillis = 3000L

    val pods = listOf(
        FusionMuPod()
    )

    val services = listOf(
        FusionMuService()
    )

    @Suppress("UnnecessaryVariable")
    fun loadConfiguration(resource: Resource): Deploy? {

        val obj = mapper.readValue(resource.file, Deploy::class.java)

        return obj
    }

    private fun getToken(): String {

        val key = "kube.api.token"

        var token = System.getProperties().getProperty(key)

        if (token == null) {
            val props = Properties()
            props.load(FileSystemResource("token").inputStream)
            token = props.getProperty(key)
        }

        return token
    }

    private fun connect(conf: Deploy): DefaultKubernetesClient {

        val token = getToken()

        val kube = InetAddress.getByName(conf.host).hostAddress

        val caCertFile = FileSystemResource(conf.caCertFile).file.absolutePath

        val config = ConfigBuilder()
            .withUsername(conf.username)
            .withPassword(token)
            .withMasterUrl("https://$kube:${conf.port}")
            .withCaCertFile(caCertFile)
            .build()

        return DefaultKubernetesClient(config)
    }

    private fun deletePod(client: DefaultKubernetesClient, name: String) {
        val deleted = client.pods().inNamespace(namespace).withName(name).delete()
        println("deleted=$deleted")
        while (true) {
            val pod = client.pods().inNamespace(namespace).withName(name).get() ?: break
            println("Waiting for pod '$name' to be deleted: ${pod.status.phase}")
            sleep(deleteWaitDelayMillis)
        }
    }

    private fun deleteService(client: DefaultKubernetesClient, name: String) {
        val deleted = client.services().inNamespace(namespace).withName(name).delete()
        println("deleted=$deleted")
        while (true) {
            val service = client.services().inNamespace(namespace).withName(name).get() ?: break
            println("Waiting for service '$name' to be deleted: ${service.status}")
            sleep(deleteWaitDelayMillis)
        }
    }

    fun doDeploy(pod: Pod, client: DefaultKubernetesClient) {

        val name = pod.metadata.name

        println("*name: $name")

        val pods = client.pods().inNamespace(namespace).list()

        pods.items.forEach { p -> println("name: ${p.metadata.name}") }

        val exist = pods.items.any { p -> name == p.metadata.name }

        if (exist) {
            deletePod(client, name)
        }

        client.pods().inNamespace(namespace).create(pod)
    }

    fun doDeploy(service: Service, client: DefaultKubernetesClient) {

        val name = service.metadata.name

        println("*name: $name")

        val services = client.services().inNamespace(namespace).list()

        services.items.forEach { p -> println("name: ${p.metadata.name}") }

        val exist = services.items.any { p -> name == p.metadata.name }

        if (exist) {
            deleteService(client, name)
        }

        client.services().inNamespace(namespace).create(service)
    }

    fun deploy(pod: Pod, client: DefaultKubernetesClient) {
        try {
            doDeploy(pod, client)
        } catch (e: Exception) {
            println(e.message + '\n' + e.printStackTrace())
        }
    }

    fun deploy(service: Service, client: DefaultKubernetesClient) {
        try {
            doDeploy(service, client)
        } catch (e: Exception) {
            println(e.message + '\n' + e.printStackTrace())
        }
    }

    fun listPods(client: DefaultKubernetesClient) {
        client.pods().inNamespace(namespace).list()
            .items.forEach { p -> println("name: " + p.metadata.name) }
    }

    override fun apply(project: Project) {

        project.tasks.register("fusionDeploy") { task ->
            task.doLast {
                println("Deploying ...")

                val resource = FileSystemResource(location)
                val conf = loadConfiguration(resource)
                println("$conf")
                if (conf == null) {
                    println("Unable to read configuration")
                    return@doLast
                }

                println("Connecting ...")

                val client = connect(conf)

                listPods(client)

                pods.forEach { pod ->
                    deploy(pod, client)
                }

                services.forEach { service ->
                    deploy(service, client)
                }

                listPods(client)

                println("Done")
            }
        }

    }

}