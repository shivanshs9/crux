package com.shivansh.crux.service

import com.shivansh.crux.controller.McqOptionData
import com.shivansh.crux.controller.McqQuestionData
import com.shivansh.crux.model.ITest
import com.shivansh.crux.model.McqOption
import com.shivansh.crux.model.McqQuestion
import com.shivansh.crux.model.ProblemSetter
import com.shivansh.crux.repository.McqOptionRepository
import com.shivansh.crux.repository.McqQuestionRepository
import com.shivansh.crux.repository.ProblemSetterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface IQuestionService {
    fun findMcqQuestionsOfTest(test: ITest): List<McqQuestion>
    fun createNewMcqQuestion(problemSetter: ProblemSetter, data: McqQuestionData): McqQuestion?
    fun findMcqQuestionById(id: Long): McqQuestion?
    fun addMcqOption(mcqQuestion: McqQuestion, data: McqOptionData): McqOption?
    fun updateMcqQuestion(question: McqQuestion, data: McqQuestionData)
    fun getMcqOptions(mcqQuestion: McqQuestion): List<McqOption>
    fun deleteMcqOption(mcqOption: McqOption)
    fun getMcqOption(optionId: Long): McqOption?
    fun getMcqQuestion(questionId: Long): McqQuestion?
}

@Service
class QuestionService: IQuestionService {
    @Autowired
    lateinit var problemSetterRepository: ProblemSetterRepository

    @Autowired
    lateinit var mcqQuestionRepository: McqQuestionRepository

    @Autowired
    lateinit var mcqOptionRepository: McqOptionRepository

    override fun findMcqQuestionsOfTest(test: ITest): List<McqQuestion> = mcqQuestionRepository.findAllByTestId(test.id).toList()

    override fun createNewMcqQuestion(problemSetter: ProblemSetter, data: McqQuestionData): McqQuestion? {
        val question = McqQuestion().apply {
            title = data.title!!
            description = data.description ?: ""
            marks = data.marks!!
            createdTime = Calendar.getInstance().time
            test = problemSetter.test
            this.problemSetter = problemSetter
        }
        return mcqQuestionRepository.save(question)
    }

    override fun findMcqQuestionById(id: Long): McqQuestion? = mcqQuestionRepository.findById(id).run {
        if (isPresent) get() else null
    }

    override fun addMcqOption(mcqQuestion: McqQuestion, data: McqOptionData): McqOption? {
        val option = McqOption().apply {
            question = mcqQuestion
            statement = data.statement!!
            isCorrect = data.isCorrect
        }
        return mcqOptionRepository.save(option)
    }

    override fun updateMcqQuestion(question: McqQuestion, data: McqQuestionData) {
        question.apply {
            title = data.title!!
            description = data.description ?: ""
            marks = data.marks!!
        }
        mcqQuestionRepository.save(question)
    }

    override fun getMcqOptions(mcqQuestion: McqQuestion): List<McqOption> = mcqOptionRepository.findAllByQuestion(mcqQuestion).toList()
    override fun deleteMcqOption(mcqOption: McqOption) = mcqOptionRepository.delete(mcqOption)
    override fun getMcqOption(optionId: Long): McqOption? = mcqOptionRepository.findById(optionId).run {
        if (isPresent) get() else null
    }

    override fun getMcqQuestion(questionId: Long): McqQuestion? = mcqQuestionRepository.findById(questionId).run {
        if (isPresent)  get() else null
    }
}