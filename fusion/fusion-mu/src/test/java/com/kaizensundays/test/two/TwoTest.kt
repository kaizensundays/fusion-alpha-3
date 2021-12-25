package com.kaizensundays.test.two

import com.kaizensundays.fusion.mu.Flights
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.blockhound.BlockHound
import reactor.blockhound.BlockingOperationError
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Duration

/**
 * Created: Sunday 11/21/2021, 12:39 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class TwoTest {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private fun monoWithSleep(): Mono<String> {
        return Mono.fromCallable {
            Thread.sleep(10)
            ""
        }
    }

    private fun monoWithRead(): Mono<String> {

        println(Flights.read("/flights.json"))

        return Mono.fromCallable {
            Thread.sleep(10)
            ""
        }
    }

    private fun <T> assertExceptionCause(type: Class<T>, block: () -> Unit) {
        try {
            block.invoke()
            fail()
        } catch (e: Exception) {
            logger.error(e.message, e)
            assertTrue(type == e.cause?.javaClass)
        }
    }

    @Before
    fun beforeAll() {
        BlockHound.install()
    }

    private fun <T> assertNoBlockingCalls(block: () -> Mono<T>) {
        Mono.defer { block.invoke() }.subscribeOn(Schedulers.parallel())
            .block(Duration.ofSeconds(10))
    }

    @Test
    fun monoWithSleepHasBlockingCall() {
        assertExceptionCause(BlockingOperationError::class.java) { assertNoBlockingCalls { monoWithSleep() } }
    }

    @Test
    fun monoWithReadHasBlockingCall() {
        assertExceptionCause(BlockingOperationError::class.java) { assertNoBlockingCalls { monoWithRead() } }
    }

}