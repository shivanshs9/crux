package com.shivansh.crux.repository

import com.shivansh.crux.model.Business
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BusinessRepository : CrudRepository<Business, Long> {
    fun findByName(name: String): Business?

    @Query("SELECT b.* FROM business b JOIN business_member bm on b.id = bm.businessId" +
            " JOIN problem_setter ps on bm.id = ps.businessMemberId WHERE ps.testId = ?1", nativeQuery = true)
    fun findByTestId(testId: Long): Business
}