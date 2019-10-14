package com.shivansh.crux.controller

import com.shivansh.crux.DataNotFoundException
import com.shivansh.crux.InvalidRoleProvidedException
import com.shivansh.crux.model.User
import com.shivansh.crux.service.IBusinessService
import com.shivansh.crux.service.IQuestionService
import com.shivansh.crux.service.ITestService
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
    lateinit var businessService: IBusinessService

    @Autowired
    lateinit var testService: ITestService

    @Autowired
    lateinit var questionService: IQuestionService

    lateinit var user: User

    internal fun getBusiness() = userService.findByUsername(securityService.findLoggedInUsername()!!)?.let {
        user = it
        businessService.findByUser(it)
    }!!.business

    internal fun getTest(id: Long) = testService.findTestWithRegistrationCountById(id) ?: throw DataNotFoundException("Given test does not exists")

    internal fun verifySetterOfMcqQuestion(testId: Long, questionId: Long) = questionService.findMcqQuestionById(questionId)?.let {
        val user = userService.findByUsername(securityService.findLoggedInUsername()!!)!!
        testService.findProblemSetterForTestAndUser(testId, user.id)?.let {problemSetter ->
            if (it.problemSetter.id != problemSetter.id)
                throw InvalidRoleProvidedException("User is not the problem setter of the given question")
        } ?: throw InvalidRoleProvidedException("User is not the problem setter of the given test")
        it
    } ?: throw DataNotFoundException("Question doesn't exists with given ID")

    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        model["title"] = "Dashboard"
        val business = getBusiness().also { model["business"] = it }
        val tests = testService.findUpcomingTestsByBusiness(user, business).also { model["tests"] = it }
        return "business-work-dashboard"
    }

    @GetMapping("/tests/{testId:[\\d]+}")
    fun getTestDetail(@PathVariable testId: Long, model: Model): String {
        val test = getTest(testId).also { model["test"] = it }
        model["title"] = "Test Detail"
        model["business"] = getBusiness()
        return "business-manage-test"
    }

    @GetMapping("/tests/{testId:[\\d]+}/questions")
    fun getTestQuestions(@PathVariable testId: Long, model: Model): String {
        val test = getTest(testId).also { model["test"] = it }
        model["title"] = "Test Questions"
        model["business"] = getBusiness()
        model["mcq_questions"] = questionService.findMcqQuestionsOfTest(test)
        return "business-manage-test-questions"
    }

    @GetMapping("/tests/{testId:[\\d]+}/questions/mcq/{questionId:[\\d]+}")
    fun getMcqQuestion(@PathVariable testId: Long, @PathVariable questionId: Long, model: Model): String {
        val question = verifySetterOfMcqQuestion(testId, questionId).also { model["question"] = it }
        model["title"] = "MCQ Question"
        model["options"] = questionService.getMcqOptions(question)
        model["business"] = getBusiness()
        model["testId"] = testId
        return "business-manage-test-mcq-question"
    }

    @GetMapping("/past-tests")
    fun getPastTests(model: Model): String {
        model["title"] = "Past Tests"
        val business = getBusiness().also { model["business"] = it }
        val tests = testService.findPastTestsByBusiness(business).also { model["tests"] = it }
        return "business-work-tests"
    }
}
