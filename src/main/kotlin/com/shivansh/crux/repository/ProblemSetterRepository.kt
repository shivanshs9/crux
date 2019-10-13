package com.shivansh.crux.repository

import com.shivansh.crux.model.ProblemSetter
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProblemSetterRepository : CrudRepository<ProblemSetter, Long> {
}