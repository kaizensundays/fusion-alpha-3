package com.kaizensundays.fusion.mu.dao

import com.kaizensundays.fusion.mu.messages.FindFlight
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*

/**
 * Created: Sunday 10/24/2021, 1:17 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class FindFlightDao(private val jdbc: NamedParameterJdbcTemplate) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val rowMapper = FindFlightRowMapper()

    fun findAll(): List<FindFlight> {
        return jdbc.query("select * from find_flight", rowMapper)
    }

    fun count() = jdbc.queryForObject("select count(*) from find_flight",
            emptyMap<String, Any>(), Int::class.java) ?: -1

    fun insert(entity: FindFlight): Int {

        val params = mapOf<String, Any>(
                "uuid" to entity.uuid,
                "user_id" to entity.user,
                "ip" to entity.ip,
                "init" to entity.from,
                "dest" to entity.to,
                "depart" to entity.depart,
                "goback" to entity.goback,
                "updated" to Date()
        )

        return jdbc.update("insert into find_flight values(:uuid, :user_id, :ip, :init, :dest, :depart, :goback, :updated)", params)
    }

}