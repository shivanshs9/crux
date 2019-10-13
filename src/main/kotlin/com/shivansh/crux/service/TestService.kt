package com.shivansh.crux.service

import com.shivansh.crux.controller.TestDetailsData
import com.shivansh.crux.model.*
import com.shivansh.crux.repository.BusinessMemberRepository
import com.shivansh.crux.repository.ProblemSetterRepository
import com.shivansh.crux.repository.TestRepository
import com.shivansh.crux.repository.TestWithRegistrationCountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface ITestService {
    fun findTestWithRegistrationCountById(id: Long): ITest?
    fun findUpcomingTests(count: Int): List<Test>
    fun findUpcomingTestsByBusiness(user: User, business: Business): List<Test>
    fun findPastTestsByBusiness(business: Business): List<ITest>
    fun createNewBusinessTest(businessMember: BusinessMember, data: TestDetailsData): Test?
    fun updateTestWithData(testId: Long, data: TestDetailsData): Test?
    fun findProblemSetterForTestAndUser(testId: Long, userId: Long): ProblemSetter?
}

@Service
class TestService : ITestService {
    @Autowired
    lateinit var testRepository: TestRepository

    @Autowired
    lateinit var testWithRegistrationCountRepository: TestWithRegistrationCountRepository

    @Autowired
    lateinit var businessMemberRepository: BusinessMemberRepository

    @Autowired
    lateinit var problemSetterRepository: ProblemSetterRepository

    internal fun getBusinessMemberIds(business: Business) = businessMemberRepository.findByBusiness(business).map { it.id }.toSet()

    internal fun mapTestsWithUserAsSetter(tests: Set<Test>, user: User, business: Business): List<Test> {
        val userTests = testRepository.findByBusinessMemberId(businessMemberRepository.findByUserAndBusiness(user, business)!!.id)
        return tests.map {
            it.isUserSetter = it in userTests
            it
        }
    }

    internal fun findTestById(id: Long): Test? = testRepository.findById(id).run {
        if (isPresent) get() else null
    }

    override fun findTestWithRegistrationCountById(id: Long): ITest? = testWithRegistrationCountRepository.findById(id).run {
        if (isPresent) get() else null
    }

    override fun findUpcomingTests(count: Int): List<Test> = testRepository.findByStartTimeGreaterThan(Calendar.getInstance().time, count).toList()

    override fun findUpcomingTestsByBusiness(user: User, business: Business): List<Test> {
        val tests = testRepository.findByBusinessMemberIdsAndStartTimeGreaterThan(getBusinessMemberIds(business), Calendar.getInstance().time)
        return mapTestsWithUserAsSetter(tests, user, business)
    }

    override fun findPastTestsByBusiness(business: Business): List<ITest> {
        return testWithRegistrationCountRepository.findByBusinessMemberIdsAndStartTimeLessThanEqualWithRegistrationCount(getBusinessMemberIds(business), Calendar.getInstance().time).toList()
    }

    override fun updateTestWithData(testId: Long, data: TestDetailsData): Test? = findTestById(testId)?.run {
        data.name?.let { name = it }
        data.startDateTime?.let { startTime = it }
        data.endDateTime?.let { endTime = it }
        data.summary?.let { summary = it }
        data.description?.let { description = it }
        testRepository.save(this)
    }

    override fun createNewBusinessTest(businessMember: BusinessMember, data: TestDetailsData): Test {
        val test = Test().apply {
            name = data.name!!
            createdTime = Calendar.getInstance().time
            startTime = data.startDateTime!!
            endTime = data.endDateTime!!
            summary = data.summary ?: ""
            description = data.description ?: ""
            organizer = businessMember.business.name
            isUserSetter = true
        }
        val problemSetter = ProblemSetter().apply {
            this.test = test
            this.businessMember = businessMember
            this.isCreator = true
            this.joinedTime = Calendar.getInstance().time
        }
        testRepository.save(test)
        problemSetterRepository.save(problemSetter)
        return test
    }

    override fun findProblemSetterForTestAndUser(testId: Long, userId: Long): ProblemSetter? = problemSetterRepository.findByTestIdAndUserId(testId, userId)
}