package com.shivansh.crux.service

import com.shivansh.crux.InvalidDataException
import com.shivansh.crux.model.*
import com.shivansh.crux.repository.McqSubmissionOptionRepository
import com.shivansh.crux.repository.McqSubmissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface ISubmissionService {
    fun findMcqSubmissionForParticipant(participant: TestParticipant): List<McqSubmission>
    fun submitMcqAnswer(participant: TestParticipant, question: McqQuestion, optionIds: List<Long>): McqSubmission
}

@Service
class SubmissionService: ISubmissionService {
    @Autowired
    lateinit var questionService: IQuestionService

    @Autowired
    lateinit var mcqSubmissionRepository: McqSubmissionRepository

    @Autowired
    lateinit var mcqSubmissionOptionRepository: McqSubmissionOptionRepository

    @Transactional
    override fun submitMcqAnswer(participant: TestParticipant, question: McqQuestion, optionIds: List<Long>): McqSubmission {
        val mcqSubmission = mcqSubmissionRepository.findByParticipantAndQuestion(participant, question)?.also {
            mcqSubmissionOptionRepository.deleteAll(it.options)
        } ?: McqSubmission()
        mcqSubmission.apply {
            this.participant = participant
            this.question = question
            this.submittedTime = Calendar.getInstance().time
            this.score = if (question.options.filter { it.isCorrect }.map { it.id }.toSet() == optionIds.toSet()) question.marks else 0
        }
        mcqSubmissionRepository.save(mcqSubmission)
        val mcqSubmissionOptions = optionIds.map {
            McqSubmissionOption().apply {
                this.id = McqSubmissionOptionId().apply {
                    this.optionId = it.toLong()
                    this.submissionId = mcqSubmission.id
                }
                this.option = questionService.getMcqOption(it.toLong()) ?: throw InvalidDataException("Option ID $it not found")
                this.submission = mcqSubmission
            }
        }
        mcqSubmissionOptionRepository.saveAll(mcqSubmissionOptions)
        return mcqSubmission
    }

    override fun findMcqSubmissionForParticipant(participant: TestParticipant): List<McqSubmission> = mcqSubmissionRepository.findByParticipant(participant).toList()
}