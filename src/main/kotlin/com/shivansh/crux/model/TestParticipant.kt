package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

interface ITestParticipant {
    var id: Long
    var registeredTime: Date
    var endTime: Date?
    var test: Test
    var user: User
    val isOver: Boolean
        get() = test.isOver || endTime?.let { it <= Calendar.getInstance().time } ?: false

    var totalScore: Long
}

@Entity
@Table(name = "participant")
class TestParticipant: ITestParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0

    override lateinit var registeredTime: Date
    override var endTime: Date? = null

    @ManyToOne
    @JoinColumn(name = "testId")
    override lateinit var test: Test

    @ManyToOne
    @JoinColumn(name = "userId")
    override lateinit var user: User

    @Transient
    override var totalScore: Long = 0
}