package io.boonlogic.soul.land.web

import com.kakawait.security.cas.DefaultProxyCallbackAndServiceAuthenticationDetails
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.cas.authentication.CasAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Controller("mainController")
class CASDemoController(
    @Value("\${security.cas.service.paths.logout}") val casLogoutUrl: String
) {

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun index(request: HttpServletRequest,
              response: HttpServletResponse, model: Model) = "index"


    @RequestMapping(value = ["/protected"], method = [RequestMethod.GET])
    fun protectedPage(authentication: Authentication?, model: Model): String {

        if (authentication != null && authentication.name.isNotEmpty()) {
            model.addAttribute("username", authentication.name);
            model.addAttribute("principal", authentication.principal)

            if(authentication is CasAuthenticationToken) {
                model.addAttribute("attributes",authentication.assertion.attributes)

            }

//            println(authentication.principal)
//            println(authentication.details)
//            println(authentication.authorities)

//            model.addAttribute("pgt", getProxyGrantingTicket(authentication).orElse(null));
        }

        return "protected"
    }

}

