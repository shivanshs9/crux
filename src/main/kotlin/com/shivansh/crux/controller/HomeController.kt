package com.shivansh.crux.controller

import com.shivansh.crux.service.ITestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.savedrequest.SavedRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpSession

@Controller
class HomeController : BaseController() {
    @Autowired
    lateinit var testService: ITestService

    @GetMapping("/")
    fun home(model: Model, session: HttpSession): String {
        model["title"] = "Home"
        (session.getAttribute("SPRING_SECURITY_SAVED_REQUEST") as? SavedRequest)?.let {
            model["redirect_to"] = it.redirectUrl
        }
        val tests = testService.findUpcomingTests(5)
        return "index"
    }
}