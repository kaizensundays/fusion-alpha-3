package com.kaizensundays.fusion.mu

import org.apache.ignite.cache.store.CacheStore
import org.apache.ignite.cache.store.CacheStoreAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import java.io.Serializable
import javax.cache.configuration.Factory

/**
 * Created: Saturday 10/23/2021, 2:05 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
abstract class AbstractCacheStore<K, V> : ApplicationContextAware, CacheStoreAdapter<K, V>(), Serializable {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private lateinit var context: ApplicationContext
    }

    override fun setApplicationContext(context: ApplicationContext) {
        AbstractCacheStore.context = context
    }

    fun factory(): Factory<CacheStore<K, V>> {
        return Factory<CacheStore<K, V>> { context.getBean(this.javaClass) }
    }

}