package com.shivansh.crux.repository

import com.shivansh.crux.model.McqQuestion
import com.shivansh.crux.model.Test
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface McqQuestionRepository: CrudRepository<McqQuestion, Long> {
    fun findAllByTest(test: Test): Set<McqQuestion>
    fun findAllByTestId(testId: Long): Set<McqQuestion>
}