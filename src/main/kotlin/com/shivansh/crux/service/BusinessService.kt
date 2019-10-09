package com.shivansh.crux.service

import com.shivansh.crux.model.Business
import com.shivansh.crux.model.BusinessMember
import com.shivansh.crux.model.User
import com.shivansh.crux.repository.BusinessMemberRepository
import com.shivansh.crux.repository.BusinessRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface IBusinessService {
    fun findByName(name: String): Business?
    fun createNewBusiness(name: String, creator: User, createdTime: Date)
    fun createNewBusiness(name: String, creator: User) {
        createNewBusiness(name, creator, Calendar.getInstance().time)
    }

    fun findByUser(user: User): BusinessMember?
}

@Service
class BusinessService : IBusinessService {
    @Autowired
    private lateinit var businessRepository: BusinessRepository

    @Autowired
    private lateinit var businessMemberRepository: BusinessMemberRepository

    override fun findByName(name: String): Business? = businessRepository.findByName(name)

    @Transactional
    override fun createNewBusiness(name: String, creator: User, createdTime: Date) {
        val business = Business().apply {
            this.name = name
            this.createdTime = createdTime
        }
        val businessMember = BusinessMember().apply {
            this.business = business
            this.user = creator
            this.joinedTime = createdTime
            this.position = BusinessMember.POSITION.Owner
        }
        businessRepository.save(business)
        businessMemberRepository.save(businessMember)
    }

    override fun findByUser(user: User): BusinessMember? = businessMemberRepository.findByUser(user)
}
