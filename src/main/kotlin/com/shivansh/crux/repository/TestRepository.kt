package com.shivansh.crux.repository

import com.shivansh.crux.model.Test
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TestRepository : CrudRepository<Test, Long> {
    @Query("SELECT * FROM test t WHERE t.startTime > ?1 ORDER BY t.startTime ASC LIMIT ?2", nativeQuery = true)
    fun findByStartTimeGreaterThan(startTime: Date, count: Int): List<Test>
}