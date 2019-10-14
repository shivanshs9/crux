package com.shivansh.crux.service

import com.shivansh.crux.controller.TestDetailsData
import com.shivansh.crux.model.*
import com.shivansh.crux.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface ITestService {
    fun findTestWithRegistrationCountById(id: Long): ITest?
    fun findUpcomingTests(count: Int): List<ITest>
    fun findUpcomingTestsByBusiness(user: User, business: Business): List<Test>
    fun findPastTestsByBusiness(business: Business): List<ITest>
    fun createNewBusinessTest(businessMember: BusinessMember, data: TestDetailsData): Test?
    fun updateTestWithData(testId: Long, data: TestDetailsData): Test?
    fun findProblemSetterForTestAndUser(testId: Long, userId: Long): ProblemSetter?
    fun findTestWithUserRegistered(user: User, testId: Long): ITest?
    fun findParticipantByUserAndTest(user: User, test: ITest): TestParticipant?
    fun addTestParticipant(user: User, test: Test): TestParticipant?
    fun removeTestParticipant(participant: TestParticipant)
    fun endTest(participant: TestParticipant)
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

    @Autowired
    lateinit var testParticipantRepository: TestParticipantRepository

    internal fun getBusinessMemberIds(business: Business) = businessMemberRepository.findByBusiness(business).map { it.id }.toSet()

    internal fun mapTestsWithUserAsParticipant(tests: Set<Test>, user: User, business: Business): List<Test> {
        val userTests = testParticipantRepository.findByUser(user).map { it.test }
        return tests.map {
            it.isUserRegistered = it in userTests
            it
        }
    }

    internal fun mapTestsWithUserAsSetter(tests: Set<Test>, user: User, business: Business): List<Test> {
        val userTests = testRepository.findByBusinessMemberId(businessMemberRepository.findByUserAndBusiness(user, business)!!.id)
        return tests.map {
            it.isUserSetter = it in userTests
            it
        }
    }

    fun mapTestWithUserAsParticipant(test: ITest, user: User) = testParticipantRepository.findByTestIdAndUser(test.id, user)?.run {
        test.isUserRegistered = true
    }

    fun findTestById(id: Long): Test? = testRepository.findById(id).run {
        if (isPresent) get() else null
    }

    override fun findTestWithRegistrationCountById(id: Long): ITest? = testWithRegistrationCountRepository.findById(id).run {
        if (isPresent) get() else null
    }

    override fun findUpcomingTests(count: Int): List<ITest> = testWithRegistrationCountRepository.findByStartTimeGreaterThan(Calendar.getInstance().time, count).toList()

    override fun findUpcomingTestsByBusiness(user: User, business: Business): List<Test> {
        val tests = testRepository.findByBusinessMemberIdsAndStartTimeGreaterThan(getBusinessMemberIds(business), Calendar.getInstance().time)
        return mapTestsWithUserAsSetter(tests, user, business)
    }

    override fun findPastTestsByBusiness(business: Business): List<ITest> {
        return testWithRegistrationCountRepository.findByBusinessMemberIdsAndStartTimeLessThanEqualWithRegistrationCount(getBusinessMemberIds(business), Calendar.getInstance().time).toList()
    }

    override fun findTestWithUserRegistered(user: User, testId: Long): ITest? = findTestWithRegistrationCountById(testId)?.let {
        mapTestWithUserAsParticipant(it, user)
        it
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

    override fun findParticipantByUserAndTest(user: User, test: ITest): TestParticipant? = testParticipantRepository.findByTestIdAndUser(test.id, user)

    override fun addTestParticipant(user: User, test: Test): TestParticipant? {
        val participant = TestParticipant().apply {
            this.registeredTime = Calendar.getInstance().time
            this.test = test
            this.user = user
        }
        return testParticipantRepository.save(participant)
    }

    override fun removeTestParticipant(participant: TestParticipant) = testParticipantRepository.delete(participant)

    override fun endTest(participant: TestParticipant) {
        participant.endTime = Calendar.getInstance().time
        testParticipantRepository.save(participant)
    }
}