package com.shivansh.crux.repository

import com.shivansh.crux.model.McqQuestion
import com.shivansh.crux.model.McqSubmission
import com.shivansh.crux.model.TestParticipant
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface McqSubmissionRepository: CrudRepository<McqSubmission, Long> {
    fun findByParticipant(participant: TestParticipant): Set<McqSubmission>
    fun findByParticipantAndQuestion(participant: TestParticipant, question: McqQuestion): McqSubmission?
}