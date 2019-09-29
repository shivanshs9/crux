package com.shivansh.crux.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/auth")
class AuthController : BaseController() {
    @GetMapping("/login")
    fun login(model: Model): String {
        return "login"
    }
}