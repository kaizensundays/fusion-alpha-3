package com.kaizensundays.fusion.mu

import com.kaizensundays.fusion.mu.messages.FindFlight
import com.kaizensundays.fusion.mu.messages.Flight
import com.kaizensundays.fusion.mu.messages.JacksonObjectConverter
import com.kaizensundays.fusion.mu.messages.JacksonSerializable
import org.apache.ignite.IgniteCache
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Created: Saturday 10/16/2021, 1:46 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class FindFlightHandler(private val cache: IgniteCache<String, FindFlight>) : Handler<FindFlight> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    val converter = JacksonObjectConverter<JacksonSerializable>()

    var flights = emptyMap<String, Array<Flight>>()

    override fun handle(event: FindFlight) {
        logger.info("handle > {}", event)

        cache.put(event.uuid, event)

        logger.info("handle <")
    }

    fun getFlights(events: Flux<FindFlight>): Flux<Flight> {

        return events
            .log()
            .map { event -> event.from + '-' + event.to }
            .doOnNext { key -> logger.info("> $key") }
            .flatMap { key ->
                val list = flights[key]?.toList()
                if (list != null) Flux.fromIterable(list) else Flux.empty()
            }

    }

    @PostConstruct
    fun start() {

        val json = Flights.read("/flights.json")

        flights = converter.toObjects(json, Flights.flightTypeRef)

        logger.info("Started")
    }

    @PreDestroy
    fun stop() {
        logger.info("Stopped")
    }

}