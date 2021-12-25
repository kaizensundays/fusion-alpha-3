package com.kaizensundays.fusion.mu.dao

import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

/**
 * Created: Sunday 11/7/2021, 1:08 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
abstract class DataLoaderTemplate(private val dataSource: DataSource) {

    private fun <E> doLoad(sink: FluxSink<E>, sql: String, rowMapper: (rs: ResultSet) -> E) {

        var connection: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            connection = dataSource.connection
            ps = connection.prepareStatement(sql)
            rs = ps.executeQuery()
            while (rs.next()) {
                val entity = rowMapper.invoke(rs)
                sink.next(entity)
            }
            sink.complete()
        } finally {
            rs?.close()
            ps?.close()
            connection?.close()
        }

    }

    fun <E> load(sql: String, rowMapper: (rs: ResultSet) -> E): Flux<E> {
        return try {
            Flux.create { sink ->
                doLoad(sink, sql, rowMapper)
            }
        } catch (e: Exception) {
            Flux.error(e)
        }
    }

}