package com.kaizensundays.fusion.mu

import com.kaizensundays.fusion.mu.dao.FindFlightDao
import com.kaizensundays.fusion.mu.dao.FindFlightLoader
import org.apache.ignite.Ignite
import org.postgresql.ds.PGPoolingDataSource
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.Ordered
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import javax.sql.DataSource

/**
 * Created: Saturday 9/25/2021, 1:44 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@Configuration
@EnableAutoConfiguration
@Import(IgniteContext::class)
class ServiceContext {

    @Bean
    open fun dataSource(): DataSource {
        val ds = PGPoolingDataSource()
        ds.serverName = "PgSql"
        ds.portNumber = 30432
        ds.user = "postgres"
        ds.password = "postgres"
        return ds
    }

    @Bean
    open fun jdbc(dataSource: DataSource) = NamedParameterJdbcTemplate(dataSource)

    @Bean
    open fun findFlightDao(jdbc: NamedParameterJdbcTemplate): FindFlightDao {
        return FindFlightDao(jdbc)
    }

    @Bean
    open fun findFlightHandler(ignite: Ignite): FindFlightHandler {
        return FindFlightHandler(ignite.getOrCreateCache(CacheName.Requests))
    }

    @Bean
    open fun frontEndWebSocketHandler(findFlightHandler: FindFlightHandler): FrontEndWebSocketHandler {
        return FrontEndWebSocketHandler(findFlightHandler)
    }

    @Bean
    open fun findFlightLoader(ignite: Ignite, dataSource: DataSource) = FindFlightLoader(ignite, dataSource)


    @Bean
    open fun handlerAdapter() = WebSocketHandlerAdapter()

    @Bean
    open fun handlerMapping(frontEndWebSocketHandler: FrontEndWebSocketHandler): HandlerMapping {
        val map = mapOf(
            "/ws/frontend" to frontEndWebSocketHandler
        )

        val mapping = SimpleUrlHandlerMapping()
        mapping.urlMap = map
        mapping.order = Ordered.HIGHEST_PRECEDENCE

        return mapping
    }

    @Bean
    fun defaultRestController(): DefaultRestController {
        return DefaultRestController()
    }

}