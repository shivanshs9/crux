package com.shivansh.crux.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Transient

@Entity
@Table(name = "test_with_registrations")
class TestWithRegistrationCount : ITest {
    @Id
    override var id: Long = 0

    override lateinit var name: String
    override lateinit var createdTime: Date
    override lateinit var summary: String
    override lateinit var description: String
    override var organizer: String? = null

    @Transient
    override var isUserSetter: Boolean = false

    @Transient
    override var isUserRegistered: Boolean = false

    override lateinit var startTime: Date
    override lateinit var endTime: Date

    override var registrationCount: Long = 0
}