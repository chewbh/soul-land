package io.boonlogic.soul_land

import org.ff4j.FF4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class SearchService {

    @Autowired
    private lateinit var ff4j: FF4j


    @GetMapping( "/", produces = arrayOf("text/html"))
    fun sayHello(): String {
        val response = StringBuilder("<html><body><ul>")
        response.append("<li> To access the <b>WebConsole</b> please go to <a href=\"./ff4j-web-console/home\">ff4j-web-console</a>")
        response.append("<li> To access the <b>REST API</b> please go to <a href=\"./api/ff4j\">api/ff4j</a>")
        response.append("<li> To access the <b>Swagger File </b> please go to <a href=\"./v2/api-docs\">/v2/api-docs</a></ul>")

        response.append("<p>Is <span style=\"color:red\">Awesome</span> feature activated ? from  ff4j.check(\"AwesomeFeature\") <span style=\"color:blue\">")
        response.append(ff4j.check("AwesomeFeature"))
        response.append("</span></body></html>")
        return response.toString()
    }

    @GetMapping("/rest/search")
    fun search(@RequestParam("text") text: String): String {
        val newSearch = ff4j.check("newSearch")
        return if(newSearch) "New search is on: $text" else "legacy search: $text"
    }

}