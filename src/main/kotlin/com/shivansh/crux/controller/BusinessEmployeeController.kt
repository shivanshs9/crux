package com.shivansh.crux.controller

import com.shivansh.crux.model.User
import com.shivansh.crux.service.IBusinessService
import com.shivansh.crux.service.ITestService
import com.shivansh.crux.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/business/work")
@Controller
class BusinessEmployeeController : BaseController() {
    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var businessService: IBusinessService

    @Autowired
    lateinit var testService: ITestService

    lateinit var user: User

    internal fun getBusiness() = userService.findByUsername(securityService.findLoggedInUsername()!!)?.let {
        user = it
        businessService.findByUser(it)
    }!!.business

    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        model["title"] = "Dashboard"
        val business = getBusiness().also { model["business"] = it }
        val tests = testService.findUpcomingTestsByBusiness(user, business).also { model["tests"] = it }
        return "business-work-dashboard"
    }

    @GetMapping("/tests/{testId:[\\d]+}")
    fun testDetail(@PathVariable testId: Long, model: Model): String {
        return "business-manage-test"
    }

    @GetMapping("/past-tests")
    fun getPastTests(model: Model): String {
        model["title"] = "Past Tests"
        val business = getBusiness().also { model["business"] = it }
        val tests = testService.findPastTestsByBusiness(business).also { model["tests"] = it }
        return "business-work-tests"
    }
}
