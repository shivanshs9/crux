package com.shivansh.crux.repository

import com.shivansh.crux.model.Test
import com.shivansh.crux.model.TestParticipant
import com.shivansh.crux.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TestParticipantRepository: CrudRepository<TestParticipant, Long> {
    fun findByTest(test: Test): Set<TestParticipant>
    fun findByTestAndUser(test: Test, user: User): TestParticipant?
    fun findByTestIdAndUser(test: Long, user: User): TestParticipant?
    fun findByUser(user: User): Set<TestParticipant>
}