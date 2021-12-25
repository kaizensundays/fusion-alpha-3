package com.kaizensundays.fusion.mu.dao

import org.postgresql.ds.PGPoolingDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Created: Saturday 11/6/2021, 1:34 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@Configuration
open class FindFlightRemoteTestContext {

    @Bean
    open fun jdbc(): NamedParameterJdbcTemplate {
        val ds = PGPoolingDataSource()
        ds.serverName = "PgSql"
        ds.portNumber = 30432
        ds.user = "postgres"
        ds.password = "postgres"
        return NamedParameterJdbcTemplate(ds)
    }

    @Bean
    open fun findFlightDao(jdbc: NamedParameterJdbcTemplate): FindFlightDao {
        return FindFlightDao(jdbc)
    }
}