package com.shivansh.crux.repository

import com.shivansh.crux.model.Business
import com.shivansh.crux.model.BusinessMember
import com.shivansh.crux.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BusinessMemberRepository : CrudRepository<BusinessMember, Long> {
    fun findByUserAndBusiness(user: User, business: Business): BusinessMember?
    fun findByUser(user: User): List<BusinessMember>
}
