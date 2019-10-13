package com.shivansh.crux.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/tests/{testId:[\\d]+}")
@Controller
class TestController : BaseController() {

    @GetMapping("/details")
    fun details(model: Model) {
        return
    }
}