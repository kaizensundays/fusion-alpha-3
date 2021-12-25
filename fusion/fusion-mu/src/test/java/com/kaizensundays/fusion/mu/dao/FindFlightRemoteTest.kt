package com.kaizensundays.fusion.mu.dao

import com.kaizensundays.fusion.mu.Flights
import com.kaizensundays.fusion.mu.MuTestSupport
import com.kaizensundays.fusion.mu.messages.FindFlight
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

/**
 * Created: Saturday 11/6/2021, 1:33 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [FindFlightRemoteTestContext::class])
class FindFlightRemoteTest : MuTestSupport() {

    @Autowired
    lateinit var dao: FindFlightDao

    var requests = emptyMap<String, Array<FindFlight>>()

    @Before
    fun before() {
        val json = Flights.read("/find-flights.json")

        requests = converter.toObjects(json, Flights.findFlightTypeRef)
    }

    @Test
    fun insert() {

        (1..100).forEach { _ ->
            requests.mergeValues().forEach { request ->
                request.uuid = UUID.randomUUID().toString()
                dao.insert(request)
            }
        }

    }

}