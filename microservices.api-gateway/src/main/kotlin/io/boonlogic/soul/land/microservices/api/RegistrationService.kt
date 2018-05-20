package io.boonlogic.soul.land.microservices.api

//import io.boonlogic.soul.land.microservices.registration.event.RegistrationEvent
import io.boonlogic.soul.land.microservices.registration.event.Registration
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Output
import org.springframework.cloud.stream.messaging.Source
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@EnableBinding(RegistrationSource::class)
@RestController
class RegistrationService(
    private val registrationSource: RegistrationSource
) {

    private val log = LoggerFactory.getLogger(RegistrationService::class.java)

    @GetMapping("/rest/reg/{name}")
    fun newRegistration(@PathVariable("name") name: String) {

        val evt = Registration.RegistrationEvent.newBuilder()
            .setId(Math.random().toString())
            .setName(name)
            .build()

        registrationSource.newReg().send(MessageBuilder.withPayload(evt).build())
        log.info("event send: {}", evt.id)
    }
}
