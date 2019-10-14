package com.shivansh.crux.repository

import com.shivansh.crux.model.McqSubmissionOption
import com.shivansh.crux.model.McqSubmissionOptionId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface McqSubmissionOptionRepository: CrudRepository<McqSubmissionOption, McqSubmissionOptionId> {
}