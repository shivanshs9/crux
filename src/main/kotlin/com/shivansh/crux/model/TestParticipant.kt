package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "participant")
class TestParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var registeredTime: Date
    var endTime: Date? = null

    @ManyToOne
    @JoinColumn(name = "testId")
    lateinit var test: Test

    @ManyToOne
    @JoinColumn(name = "userId")
    lateinit var user: User

    val isOver: Boolean
        get() = test.isOver || endTime?.let { it <= Calendar.getInstance().time } ?: false
}