package com.kaizensundays.fusion.mu

import com.kaizensundays.fusion.mu.dao.FindFlightDao
import com.kaizensundays.fusion.mu.messages.FindFlight
import org.apache.ignite.cache.CacheAtomicityMode
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.events.EventType
import org.apache.ignite.logger.slf4j.Slf4jLogger
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit
import javax.cache.expiry.CreatedExpiryPolicy
import javax.cache.expiry.Duration

/**
 * Created: Saturday 9/25/2021, 1:53 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@Configuration
open class IgniteContext {

    @Bean
    open fun findFlightCacheStore(dao: FindFlightDao) = FindFlightCacheStore(dao)

    @Bean
    open fun ignite(findFlightCacheStore: FindFlightCacheStore): IgniteFactoryBean {

        val configuration = IgniteConfiguration()
                .setGridLogger(Slf4jLogger())
                .setDiscoverySpi(
                        TcpDiscoverySpi()
                                .setLocalPort(47701)
                                .setLocalPortRange(2)
                                .setIpFinder(TcpDiscoveryVmIpFinder().setAddresses(listOf("127.0.0.1:47701..47702")))
                )
                .setCacheConfiguration(
                        CacheConfiguration<String, FindFlight>()
                                .setName(CacheName.Requests)
                                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration(TimeUnit.DAYS, 1)))
                                .setCacheStoreFactory(findFlightCacheStore.factory())
                                .setReadThrough(true)
                                .setWriteThrough(true),
                        CacheConfiguration<String, String>()
                                .setName(CacheName.Results)
                                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration(TimeUnit.DAYS, 1))),
                )
                .setIncludeEventTypes(
                        EventType.EVT_CACHE_OBJECT_PUT
                )

        return IgniteFactoryBean(configuration)
    }

}