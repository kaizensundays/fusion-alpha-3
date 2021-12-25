package com.kaizensundays.fusion.mu

import com.kaizensundays.fusion.mu.messages.FindFlight
import com.kaizensundays.fusion.mu.messages.JacksonObjectConverter
import com.kaizensundays.fusion.mu.messages.JacksonSerializable
import org.apache.ignite.IgniteCache
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import reactor.blockhound.BlockHound
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Created: Sunday 11/28/2021, 12:44 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@Suppress("MemberVisibilityCanBePrivate")
class FindFlightHandlerTest {

    val converter = JacksonObjectConverter<JacksonSerializable>()

    val cache: IgniteCache<String, FindFlight> = mock()

    val handler = FindFlightHandler(cache)

    var requests = emptyList<FindFlight>()

    @Before
    fun before() {
        BlockHound.install()
        handler.start()
        requests = getFindFlightList()
    }

    fun getFindFlightList(): List<FindFlight> {

        val json = Flights.read("/find-flights.json")

        val map = converter.toObjects(json, Flights.findFlightTypeRef)

        val list = map["requests"]?.toList()
        assertNotNull(list)

        return list
    }

    fun <T> assertNoBlockingCalls(block: () -> Flux<T>) {
        Flux.defer { block.invoke() }.subscribeOn(Schedulers.parallel())
            .blockLast(Duration.ofSeconds(10))
    }

    @Test
    fun getFlights() {

        val events = Flux.fromIterable(requests)

        val flights = handler.getFlights(events)
            .collectList()
            .block(Duration.ofSeconds(10))

        assertNotNull(flights)
        assertTrue(flights.isNotEmpty())
    }

    @Test
    fun getFlightsHasNoBlockingCalls() {

        val events = Flux.fromIterable(requests)

        assertNoBlockingCalls { handler.getFlights(events) }
    }

}