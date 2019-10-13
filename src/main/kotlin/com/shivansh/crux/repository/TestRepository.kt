package com.shivansh.crux.repository

import com.shivansh.crux.model.Test
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TestRepository : CrudRepository<Test, Long> {
    @Query("SELECT * FROM test t WHERE t.startTime > ?1 ORDER BY t.startTime ASC LIMIT ?2", nativeQuery = true)
    fun findByStartTimeGreaterThan(startTime: Date, count: Int): Set<Test>

    @Query("SELECT DISTINCT * FROM test t, problem_setter ps WHERE t.id = ps.testId AND ps.businessMemberId IN ?1", nativeQuery = true)
    fun findByBusinessMemberIds(businessMemberIds: Set<Long>): Set<Test>

    @Query("SELECT t.* FROM test t, problem_setter ps WHERE t.id = ps.testId AND ps.businessMemberId = ?1", nativeQuery = true)
    fun findByBusinessMemberId(businessMemberId: Long): Set<Test>

    @Query("SELECT DISTINCT * FROM test t, problem_setter ps WHERE t.id = ps.testId AND ps.businessMemberId IN :ids AND t.startTime > :minTime", nativeQuery = true)
    fun findByBusinessMemberIdsAndStartTimeGreaterThan(@Param("ids") businessMemberIds: Set<Long>, @Param("minTime") minTime: Date): Set<Test>

    @Query("SELECT DISTINCT t.* FROM test t, problem_setter ps WHERE t.id = ps.testId AND ps.businessMemberId IN :ids AND t.startTime <= :maxTime", nativeQuery = true)
    fun findByBusinessMemberIdsAndStartTimeLessThanEqual(@Param("ids") businessMemberIds: Set<Long>, @Param("maxTime") maxTime: Date): Set<Test>
}