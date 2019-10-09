package com.shivansh.crux.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/business/admin")
class BusinessAdminController : BaseController() {
    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        model["title"] = "Dashboard"
        return "business-admin-dashboard"
    }
}