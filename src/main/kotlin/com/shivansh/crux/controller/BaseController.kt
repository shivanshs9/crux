package com.shivansh.crux.controller

import com.shivansh.crux.model.User
import com.shivansh.crux.service.ISecurityService
import com.shivansh.crux.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.ModelAttribute
import javax.servlet.http.HttpServletRequest

open class BaseController {
    @Autowired
    lateinit var securityService: ISecurityService

    @Autowired
    lateinit var userService: IUserService

    @Value("\${crux.app_title}")
    private lateinit var appTitle: String

    @ModelAttribute("app_title")
    fun getAppTitle(): String = appTitle

    @ModelAttribute
    fun getLoginInfo(model: Model) {
        model["is_logged_in"] = false
        securityService.findLoggedInUserDetails()?.let {
            model["username"] = it.username
            model["roles"] = it.authorities.map { it.authority }
            model["is_logged_in"] = true
        }
    }

    @ModelAttribute
    fun getRequestInfo(model: Model, request: HttpServletRequest) {
        model["url"] = request.requestURL
        model["path"] = request.requestURI
        model["_csrf"] = request.getAttribute("_csrf")
    }

    fun getLoggedInUser(): User? = userService.findByUsername(securityService.findLoggedInUsername()!!)
}
