package com.shivansh.crux.repository

import com.shivansh.crux.model.TestWithRegistrationCount
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TestWithRegistrationCountRepository : CrudRepository<TestWithRegistrationCount, Long> {
    @Query("SELECT DISTINCT t.* FROM test_with_registrations t, problem_setter ps WHERE t.id = ps.testId AND ps.businessMemberId IN :ids AND t.startTime <= :maxTime", nativeQuery = true)
    fun findByBusinessMemberIdsAndStartTimeLessThanEqualWithRegistrationCount(@Param("ids") businessMemberIds: Set<Long>, @Param("maxTime") maxTime: Date): Set<TestWithRegistrationCount>

    @Query("SELECT * FROM test_with_registrations t WHERE t.startTime > ?1 ORDER BY t.startTime LIMIT ?2", nativeQuery = true)
    fun findByStartTimeGreaterThan(startTime: Date, count: Int): Set<TestWithRegistrationCount>

    @Query("SELECT DISTINCT t.* FROM test_with_registrations t JOIN participant_with_score p on t.id = p.testId WHERE p.userId = :userId ORDER BY totalScore DESC", nativeQuery = true)
    fun findByRegisteredUser(@Param("userId") userId: Long): Set<TestWithRegistrationCount>
}