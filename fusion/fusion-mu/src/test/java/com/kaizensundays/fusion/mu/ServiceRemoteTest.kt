package com.kaizensundays.fusion.mu

import com.kaizensundays.fusion.mu.messages.FindFlight
import com.kaizensundays.fusion.mu.messages.Flight
import org.junit.Before
import org.junit.Test
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Flux
import java.net.URI
import java.time.Duration
import java.util.*
import kotlin.test.assertEquals

/**
 * Created: Monday 9/27/2021, 1:50 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@Suppress("MemberVisibilityCanBePrivate")
class ServiceRemoteTest : MuTestSupport() {

    val port = 7701

    val client = ReactorNettyWebSocketClient()

    var requests = emptyMap<String, Array<FindFlight>>()

    @Before
    fun before() {
        val json = Flights.read("/find-flights.json")

        requests = converter.toObjects(json, Flights.findFlightTypeRef)

        requests.mergeValues().forEach { request -> request.uuid = UUID.randomUUID().toString() }
    }

    fun requests(): Flux<String> {
        return Flux.fromIterable(requests.mergeValues())
            .delaySubscription(Duration.ofSeconds(1))
            .delayElements(Duration.ofSeconds(1))
            .map { request -> converter.fromObject(request) }
    }

    @Test
    fun test() {

        val flights = mutableListOf<Flight>()

        client.execute(URI("ws://localhost:$port/ws/frontend")) { session ->

            session.send(
                requests()
                    .log()
                    .map { s -> session.textMessage(s) }
            ).thenMany(
                session.receive()
                    .take(4)
                    .map { m -> m.payloadAsText }
                    .log()
                    .map { json -> converter.toObject(json) }
                    .filter { event -> event is Flight }
                    .map { event -> flights.add(event as Flight) }
                    .then()
            ).then()
        }.block(Duration.ofSeconds(100))

        assertEquals(4, flights.size)
    }

}