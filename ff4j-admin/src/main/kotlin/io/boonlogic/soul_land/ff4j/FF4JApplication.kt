package io.boonlogic.soul_land.ff4j

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class FF4JApplication {

    @GetMapping("/", produces = arrayOf("text/html"))
    fun index(): String {
        return """
            |<html>
            |   <body>
            |       <ul>
            |           <li>
            |               To access the <b>WebConsole</b>
            |               please go to <a href=\"./ff4j-web-console/home\">ff4j-web-console</a>
            |           </li>
            |           <li>
            |               To access the <b>REST API</b>
            |               please go to <a href=\"./api/ff4j\">api/ff4j</a>
            |           </li>
            |           <li>
            |           <li>
            |               To access the <b>Swagger File </b>
            |               please go to <a href=\"./v2/api-docs\">/v2/api-docs</a>
            |           </li>
            |       </ul>
            |   </body>
            |</html>
        """.trimMargin("|")
    }
}

fun main(args: Array<String>){
    SpringApplication.
        run(
        FF4JApplication::class.java)
}
