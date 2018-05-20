package io.boonlogic.soul.land.microservices.api

import org.springframework.cloud.stream.annotation.Output
import org.springframework.messaging.MessageChannel

interface RegistrationSource {

    @Output("registration")
    fun newReg(): MessageChannel
}
