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
}