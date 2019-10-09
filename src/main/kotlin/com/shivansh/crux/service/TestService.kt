package com.shivansh.crux.service

import com.shivansh.crux.model.Test
import com.shivansh.crux.repository.TestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface ITestService {
    fun findUpcomingTests(count: Int): List<Test>
}

@Service
class TestService : ITestService {
    @Autowired
    lateinit var testRepository: TestRepository

    override fun findUpcomingTests(count: Int): List<Test> = testRepository.findByStartTimeGreaterThan(Calendar.getInstance().time, count)
}