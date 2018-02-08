package io.boonlogic.soul_land

import org.ff4j.FF4j
import org.ff4j.core.FlippingExecutionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class SearchService {

    @Autowired
    private lateinit var ff4j: FF4j

    @GetMapping("/rest/search")
    fun search(@RequestParam("text") text: String): String {

        val fex = FlippingExecutionContext()
        fex.addValue("user", "boonheng")
        val newSearch = ff4j.check("newSearch", fex)
        return if(newSearch) "New search is on: $text" else "legacy search: $text"
    }

}