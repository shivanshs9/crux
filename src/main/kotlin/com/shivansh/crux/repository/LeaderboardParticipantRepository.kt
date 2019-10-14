package com.shivansh.crux.repository

import com.shivansh.crux.model.LeaderboardParticipant
import com.shivansh.crux.model.Test
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LeaderboardParticipantRepository: CrudRepository<LeaderboardParticipant, Long> {
    fun findByTest(test: Test): Set<LeaderboardParticipant>
    fun findByTestId(testId: Long): Set<LeaderboardParticipant>

    @Query("SELECT * FROM participant_with_score WHERE testId = ?1 ORDER BY totalScore DESC ", nativeQuery = true)
    fun getLeaderboardResults(testId: Long): Set<LeaderboardParticipant>
}
