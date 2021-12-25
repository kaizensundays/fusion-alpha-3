package com.kaizensundays.fusion.mu.messages

import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

/**
 * Created: Monday 10/11/2021, 1:15 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class ObjectConversionTest {

    private val converter = JacksonObjectConverter<Event>()

    @Test
    fun convertLocalDate() {

        val json =
            """{"@class":"com.kaizensundays.fusion.mu.messages.FindFlight","user":"","ip":"","from":"MIA","to":"LAX","depart":"2021-10-11","goback":"2021-10-17","uuid":"99fd5524-7af2-4092-b3ad-1325b47869c2"}"""

        val obj = FindFlight(
            "", "", "MIA", "LAX",
            LocalDate.of(2021, 10, 11),
            LocalDate.of(2021, 10, 17),
            "99fd5524-7af2-4092-b3ad-1325b47869c2"
        )

        val s = converter.fromObject(obj)
        assertEquals(json, s)

        val o = converter.toObject(s, FindFlight::class.java)
        assertEquals(obj, o)
    }

}