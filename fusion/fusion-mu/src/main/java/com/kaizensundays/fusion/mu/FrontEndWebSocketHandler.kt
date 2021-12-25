package com.kaizensundays.fusion.mu

import com.kaizensundays.fusion.mu.messages.Event
import com.kaizensundays.fusion.mu.messages.FindFlight
import com.kaizensundays.fusion.mu.messages.Flight
import com.kaizensundays.fusion.mu.messages.JacksonObjectConverter
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

/**
 * Created: Monday 9/27/2021, 1:27 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class FrontEndWebSocketHandler(private val handler: FindFlightHandler) : WebSocketHandler {

    private val converter = JacksonObjectConverter<Event>()

    fun handleEvent(event: Event): Flux<Flight> {

        return when (event) {
            is FindFlight ->
                handler.getFlights(Flux.just(event))
            else ->
                Flux.empty()
        }
    }

    override fun handle(session: WebSocketSession): Mono<Void> {

        val subscriber = session.receive()
                .map { msg -> msg.payloadAsText }
                .log()
                .publishOn(Schedulers.elastic())
                .map { json -> converter.toObject(json) }
                .flatMap { event -> handleEvent(event) }
                .map { flight -> converter.fromObject(flight) }
                .log()
                .map { json -> session.textMessage(json) }


        return session.send(subscriber)
    }

}