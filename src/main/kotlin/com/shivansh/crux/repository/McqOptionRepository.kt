package com.shivansh.crux.repository

import com.shivansh.crux.model.McqOption
import com.shivansh.crux.model.McqQuestion
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface McqOptionRepository: CrudRepository<McqOption, Long> {
    fun findAllByQuestion(question: McqQuestion): Set<McqOption>
}
