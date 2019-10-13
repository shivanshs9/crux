package com.shivansh.crux.repository

import com.shivansh.crux.model.ProblemSetter
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProblemSetterRepository : CrudRepository<ProblemSetter, Long> {
    @Query("SELECT ps.businessMemberId FROM problem_setter ps WHERE ps.testId = :testId", nativeQuery = true)
    fun findBusinessMemberIdsByTestId(@Param("testId") testId: Long): Set<Long>

    @Query("SELECT ps.* FROM problem_setter ps JOIN business_member bm on ps.businessMemberId = bm.id WHERE ps.testId = :testId AND bm.userId = :userId", nativeQuery = true)
    fun findByTestIdAndUserId(@Param("testId") testId: Long, @Param("userId") userId: Long): ProblemSetter?
}