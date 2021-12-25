package com.kaizensundays.fusion.mu

import com.kaizensundays.fusion.mu.Flights.adjustFormat
import com.kaizensundays.fusion.mu.Flights.flightTypeRef
import com.kaizensundays.fusion.mu.Flights.read
import com.kaizensundays.fusion.mu.messages.Flight
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created: Saturday 10/30/2021, 1:30 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class FlightConverterTest : MuTestSupport() {

    @Test
    fun fromObjects() {

        val flights = mapOf(
            "LAX-LAS" to arrayOf(
                Flight("AA2002", "AA", 128.0, 715, 826),
                Flight("UA2125", "UA", 133.0, 820, 937)
            ),
            "LAX-PHX" to arrayOf(
                Flight("AA767", "AA", 177.0, 1152, 1418),
                Flight("UA5411", "UA", 182.0, 800, 1027)
            )
        )

        var wire = converter.fromObjects(flights)

        wire = wire.adjustFormat()

        val json = read("/flights.json")

        assertEquals(json, wire)

        val map = converter.toObjects(wire, flightTypeRef)

        assertEquals(2, map.size)
    }

}