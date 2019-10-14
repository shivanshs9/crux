package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "participant_with_score")
class LeaderboardParticipant: ITestParticipant {
    @Id
    override var id: Long = 0

    override lateinit var registeredTime: Date
    override var endTime: Date? = null

    @ManyToOne
    @JoinColumn(name = "testId")
    override lateinit var test: Test

    @ManyToOne
    @JoinColumn(name = "userId")
    override lateinit var user: User

    override var totalScore: Long = 0
}