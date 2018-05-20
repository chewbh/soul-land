package io.boonlogic.soul.land.microservices.hello.services

import io.boonlogic.soul.land.microservices.hello.dao.CarDAO

class CarService(val helloCount: Int) : CarDAO {

    override fun getHello(): Int = helloCount
}
