package com.shivansh.crux.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.ModelAttribute

open class BaseController {
    @Value("\${crux.app_title}")
    private lateinit var appTitle: String

    @ModelAttribute("app_title")
    fun getAppTitle(): String = appTitle
}
