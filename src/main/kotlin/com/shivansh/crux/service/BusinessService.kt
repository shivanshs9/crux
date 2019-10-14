package com.shivansh.crux.service

import com.shivansh.crux.controller.BusinessDetailsData
import com.shivansh.crux.model.Business
import com.shivansh.crux.model.BusinessMember
import com.shivansh.crux.model.ITest
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
    fun findById(id: Long): Business?
    fun findByUserAndBusinessId(user: User, businessId: Long): BusinessMember?
    fun updateBusinessDetails(id: Long, data: BusinessDetailsData): Boolean
    fun findBusinessMembers(business: Business): List<BusinessMember>
    fun addNewMember(business: Business, user: User, position: BusinessMember.POSITION): BusinessMember?
    fun removeMember(memberId: Long)
    fun findByTest(test: ITest): Business
}

@Service
class BusinessService : IBusinessService {
    @Autowired
    private lateinit var businessRepository: BusinessRepository

    @Autowired
    private lateinit var businessMemberRepository: BusinessMemberRepository

    override fun findByName(name: String): Business? = businessRepository.findByName(name)

    override fun findById(id: Long): Business? = businessRepository.findById(id).run {
        if (isPresent) get() else null
    }

    override fun findByUserAndBusinessId(user: User, businessId: Long): BusinessMember? = businessMemberRepository.findByUserAndBusinessId(user, businessId)

    override fun updateBusinessDetails(id: Long, data: BusinessDetailsData): Boolean = businessRepository.findById(id).run {
        if (isPresent) {
            val business = get()
            business.apply {
                name = data.name!!
                category = data.category
                hqState = data.state
                hqCity = data.city
                hqAddress = data.address
                hqCountry = data.country
                logo = data.logo
            }
            businessRepository.save(business)
            true
        } else false
    }

    override fun findBusinessMembers(business: Business): List<BusinessMember> = businessMemberRepository.findByBusiness(business)

    @Transactional
    override fun createNewBusiness(name: String, creator: User, createdTime: Date) {
        val business = Business().apply {
            this.name = name
            this.createdTime = createdTime
            this.hqCountry = "IN"
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

    override fun addNewMember(business: Business, user: User, position: BusinessMember.POSITION): BusinessMember? {
        if (businessMemberRepository.findByUser(user) != null) return null
        val businessMember = BusinessMember().apply {
            this.business = business
            this.user = user
            this.joinedTime = Calendar.getInstance().time
            this.position = position
        }
        businessMemberRepository.save(businessMember)
        return businessMember
    }

    override fun removeMember(memberId: Long) {
        businessMemberRepository.deleteById(memberId)
    }

    override fun findByUser(user: User): BusinessMember? = businessMemberRepository.findByUser(user)

    override fun findByTest(test: ITest) = businessRepository.findByTestId(test.id)
}
