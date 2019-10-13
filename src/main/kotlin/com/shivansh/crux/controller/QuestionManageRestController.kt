package com.shivansh.crux.controller

import com.shivansh.crux.DataNotFoundException
import com.shivansh.crux.InvalidDataException
import com.shivansh.crux.InvalidRoleProvidedException
import com.shivansh.crux.model.McqOption
import com.shivansh.crux.model.ProblemSetter
import com.shivansh.crux.service.IQuestionService
import com.shivansh.crux.service.ITestService
import com.shivansh.crux.service.IUserService
import com.shivansh.crux.util.BaseResponseData
import com.shivansh.crux.util.RequestData
import com.shivansh.crux.util.isNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tests/{testId:[\\d]+}/questions")
class QuestionManageRestController: BaseController() {
    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var testService: ITestService

    @Autowired
    lateinit var questionService: IQuestionService

    lateinit var problemSetter: ProblemSetter

    @ModelAttribute
    internal fun verifyProblemSetter(@PathVariable testId: Long) {
        val user = userService.findByUsername(securityService.findLoggedInUsername()!!)!!
        testService.findProblemSetterForTestAndUser(testId, user.id)?.let {
            problemSetter = it
        } ?: throw InvalidRoleProvidedException("User is not problem setter of the given test")
    }

    internal fun verifySetterOfMcqQuestion(questionId: Long) = questionService.findMcqQuestionById(questionId)?.let {
        if (it.problemSetter.id != problemSetter.id)
            throw InvalidRoleProvidedException("User is not the problem setter of the given question")
        it
    } ?: throw DataNotFoundException("Question doesn't exists with given ID")

    internal fun verifySetterOfMcqQuestionOption(questionId: Long, optionId: Long): McqOption? {
        verifySetterOfMcqQuestion(questionId)
        return questionService.getMcqOption(optionId)?.let {
            if (it.question.id == questionId) it else throw InvalidRoleProvidedException("Option doesn't belong to the given question")
        } ?: throw DataNotFoundException("Option doesn't exists with given ID")
    }

    @PostMapping("/mcq")
    fun createMcqQuestion(@ModelAttribute data: McqQuestionData): BaseResponseData {
        data.validate()
        return if (data.isValid()) {
            questionService.createNewMcqQuestion(problemSetter, data)
            data
        } else throw InvalidDataException(data.errors)
    }

    @PatchMapping("/mcq/{questionId:[\\d]+}")
    fun editMcqOption(@PathVariable questionId: Long, @ModelAttribute data: McqQuestionData): BaseResponseData {
        val question = verifySetterOfMcqQuestion(questionId)
        data.validate()
        return if (data.isValid()) {
            questionService.updateMcqQuestion(question, data)
            data
        } else throw InvalidDataException(data.errors)
    }

    @PostMapping("/mcq/{questionId:[\\d]+}/options")
    fun addMcqOption(@PathVariable questionId: Long, @ModelAttribute data: McqOptionData): BaseResponseData {
        val question = verifySetterOfMcqQuestion(questionId)
        data.validate()
        return if (data.isValid()) {
            questionService.addMcqOption(question, data)
            data
        } else throw InvalidDataException(data.errors)
    }

    @DeleteMapping("/mcq/{questionId:[\\d]+}/options/{optionId:[\\d]+}")
    fun removeMcqOption(@PathVariable questionId: Long, @PathVariable optionId: Long): BaseResponseData {
        verifySetterOfMcqQuestionOption(questionId, optionId)?.let {
            questionService.deleteMcqOption(it)
        }
        return RequestData().apply { invalidField("detail", "Successfully deleted") }
    }
}

data class McqQuestionData(
        val title: String?, val description: String?, val marks: Int?
): RequestData() {
    override fun validate() {
        super.validate()
        if (title.isNullOrBlank())  invalidField("title", "Title cannot be blank")
        if (marks.isNull() || marks == 0) invalidField("marks", "Marks cannot be 0 or empty")
    }
}

data class McqOptionData(val statement: String?, val isCorrect: Boolean = false): RequestData() {
    override fun validate() {
        super.validate()
        if (statement.isNullOrBlank())  invalidField("statement", "Option statement cannot be blank")
    }
}