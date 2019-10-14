package com.shivansh.crux.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.shivansh.crux.DataNotFoundException
import com.shivansh.crux.InvalidDataException
import com.shivansh.crux.InvalidRoleProvidedException
import com.shivansh.crux.TestMarkedOverException
import com.shivansh.crux.model.ITest
import com.shivansh.crux.model.toModel
import com.shivansh.crux.service.IBusinessService
import com.shivansh.crux.service.IQuestionService
import com.shivansh.crux.service.ISubmissionService
import com.shivansh.crux.service.ITestService
import com.shivansh.crux.util.BaseResponseData
import com.shivansh.crux.util.RequestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

@RequestMapping("/tests/{testId:[\\d]+}")
@Controller
class TestController : BaseController() {
    @Autowired
    lateinit var testService: ITestService

    @Autowired
    lateinit var businessService: IBusinessService

    @Autowired
    lateinit var questionService: IQuestionService

    @Autowired
    lateinit var submissionService: ISubmissionService

    lateinit var test: ITest

    @ModelAttribute
    fun getTest(@PathVariable testId: Long, model: Model) = testService.findTestWithUserRegistered(getLoggedInUser()!!, testId)?.let {
        test = it
        model["test"] = test
    } ?: throw DataNotFoundException("No test found with given ID")

    fun getBusiness() = businessService.findByTest(test)

    @GetMapping("/summary")
    fun details(model: Model): String {
        testService.findParticipantByUserAndTest(getLoggedInUser()!!, test)?.also { model["participant"] = it }
        model["title"] = test.name
        model["business"] = getBusiness()
        return "test-summary"
    }

    @GetMapping("/solve")
    fun solve(model: Model): String {
        val participant = testService.findParticipantByUserAndTest(getLoggedInUser()!!, test) ?: throw InvalidRoleProvidedException("User is not registered with the test")
        if (participant.isOver) throw TestMarkedOverException()
        model["title"] = "Solve ${test.name}"
        val questions = questionService.findMcqQuestionsOfTest(test).also { model["questions"] = it }
        model["submissions"] = submissionService.findMcqSubmissionForParticipant(participant).map {
            mapOf("id" to it.question.id, "options" to it.options.map { it.id.optionId })
        }.run { ObjectMapper().writeValueAsString(this) }
        return "test-solve"
    }

    @GetMapping("/results")
    fun userTestResults(model: Model): String {
        val participant = testService.findParticipantByUserAndTest(getLoggedInUser()!!, test) ?: throw InvalidRoleProvidedException("User is not registered with the test")
        if (!participant.isOver)    return "redirect:/tests/${test.id}/solve"
        model["title"] = "${test.name} Results"
        val questions = questionService.findMcqQuestionsOfTest(test).also { model["questions"] = it }
        model["submissions"] = submissionService.findMcqSubmissionForParticipant(participant).map {
            mapOf("id" to it.question.id, "options" to it.options.map { it.id.optionId }, "score" to it.score)
        }.run { ObjectMapper().writeValueAsString(this) }
        return "test-results"
    }

    @GetMapping("/leaderboard")
    fun leaderboard(model: Model): String {
        model["leaderboard"] = testService.getTestLeaderboard(test)
        model["title"] = "Leaderboard"
        return "test-leaderboard"
    }
}

@RequestMapping("/tests/{testId:[\\d]+}")
@RestController
class TestRestController: BaseController() {
    @Autowired
    lateinit var testService: ITestService

    @Autowired
    lateinit var questionService: IQuestionService

    @Autowired
    lateinit var submissionService: ISubmissionService

    lateinit var test: ITest

    @ModelAttribute
    fun getTest(@PathVariable testId: Long) = testService.findTestWithRegistrationCountById(testId)?.let {
        test = it
    } ?: throw DataNotFoundException("No test found with given ID")

    fun getAndVerifyUserIsParticipant() = testService.findParticipantByUserAndTest(getLoggedInUser()!!, test) ?: throw InvalidRoleProvidedException("User is not registered with this test")

    @PostMapping("/questions/mcq/{questionId:[\\d]+}/submit", consumes = ["application/json"])
    fun submitOption(@PathVariable questionId: Long, @RequestBody data: McqSubmissionData): BaseResponseData {
        val participant = getAndVerifyUserIsParticipant()
        if (participant.isOver) throw TestMarkedOverException()
        questionService.getMcqQuestion(questionId)?.let { question ->
            data.validate()
            if (question.test.id != test.id)
                data.invalidField("questionId", "Question doesn't belong to the given test")
            return if (data.isValid()) {
                submissionService.submitMcqAnswer(participant, question, data.ids!!.map { it.toLong() })
                data
            } else throw InvalidDataException(data.errors)
        } ?: throw DataNotFoundException("Question with given ID not found")
    }

    @RequestMapping("/participants", method = [RequestMethod.POST, RequestMethod.DELETE])
    fun participants(request: HttpServletRequest): BaseResponseData {
        val data = RequestData()
        val user = getLoggedInUser()
        val participant = testService.findParticipantByUserAndTest(user!!, test)

        if (test.startTime <= Calendar.getInstance().time)
            data.invalidField("time", "Test has already started")

        if (request.method == RequestMethod.POST.name) {
            if (participant != null)
                data.invalidField("participant", "User is already a participant of this test")

            return if (data.isValid()) {
                testService.addTestParticipant(user, test.toModel())
                data
            } else throw InvalidDataException(data.errors)
        } else {
            if (participant == null)
                data.invalidField("participant", "User is not a participant of this test")

            return if (data.isValid()) {
                testService.removeTestParticipant(participant!!)
                data
            } else throw InvalidDataException(data.errors)
        }
    }

    @PostMapping("/finish")
    fun end(): BaseResponseData {
        val user = getLoggedInUser()
        val participant = getAndVerifyUserIsParticipant()
        if (participant.isOver) throw TestMarkedOverException()
        testService.endTest(participant)
        return RequestData().apply { invalidField("details", "Test marked done") }
    }
}

data class McqSubmissionData(val ids: Array<String>?): RequestData() {
    override fun validate() {
        super.validate()
        if (ids.isNullOrEmpty()) invalidField("ids", "IDs cannot be provided empty")
    }
}