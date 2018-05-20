package io.boonlogic.soul.land.microservices.registration.event

import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink
import java.io.IOException

@EnableBinding(Sink::class)
class RegistrationEventStream {

    private val log = LoggerFactory.getLogger(RegistrationEventStream::class.java)

    @StreamListener(Sink.INPUT)
    fun listen(evt: RegistrationEvent) {
        log.info("received evt: {} | {}", evt.id, evt.name)
        Thread.sleep(1000 * 30 * 1)

        throw IOException("error")
        // log.info("done: {} | {}", evt.id, evt.name)
    }
}
