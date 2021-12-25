package com.kaizensundays.fusion.mu.messages

import java.time.LocalDate

/**
 * Created: Sunday 10/10/2021, 1:47 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
data class FindFlight(
        val user: String,
        val ip: String,
        val from: String,
        val to: String,
        val depart: LocalDate,
        val goback: LocalDate,
        var uuid: String = ""
) : Event