package com.shivansh.crux.model

import com.shivansh.crux.util.formatCompleteDate
import com.shivansh.crux.util.formatTimeDuration
import java.util.*
import javax.persistence.*

interface ITest {
    var id: Long

    var name: String
    var createdTime: Date
    var summary: String
    var description: String

    var organizer: String?

    var isUserSetter: Boolean

    var startTime: Date
    var endTime: Date

    private val duration: Long
        get() = endTime.time - startTime.time

    val durationString: String
        get() = formatTimeDuration(duration)

    val startTimeString: String
        get() = formatCompleteDate(startTime)

    val endTimeString: String
        get() = formatCompleteDate(endTime)

    var registrationCount: Long
}

@Entity
@Table(name = "test")
open class Test : ITest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0

    override lateinit var name: String
    override lateinit var createdTime: Date
    override lateinit var summary: String
    override lateinit var description: String

    override var organizer: String? = null

    @Transient
    override var isUserSetter: Boolean = false

    override lateinit var startTime: Date
    override lateinit var endTime: Date

    @Transient
    override var registrationCount: Long = 0
}