package com.kaizensundays.fusion.mu

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteSpring
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.IgniteConfiguration
import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Created: Saturday 10/23/2021, 1:28 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class IgniteFactoryBean(private val configuration: IgniteConfiguration) : ApplicationContextAware, FactoryBean<Ignite> {

    private lateinit var ignite: Ignite

    private lateinit var context: ApplicationContext

    override fun setApplicationContext(context: ApplicationContext) {
        this.context = context
    }

    override fun getObject(): Ignite {
        return ignite
    }

    override fun getObjectType(): Class<*> {
        return Ignite::class.java
    }

    override fun isSingleton(): Boolean {
        return true
    }

    @PostConstruct
    fun start() {
        ignite = IgniteSpring.start(configuration, context)
    }

    @PreDestroy
    fun stop() {
        Ignition.stopAll(false)
    }

}