package io.boonlogic.soul.land.microservices.registration.config

import org.springframework.messaging.converter.AbstractMessageConverter
import org.springframework.util.MimeType

class ProtobufMessageConverter :
    AbstractMessageConverter(MimeType("application")) {

    override fun supports(clazz: Class<*>?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
