package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "test")
class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var name: String
    lateinit var createdTime: Date
    lateinit var summary: String
    lateinit var description: String
    lateinit var startTime: Date
    lateinit var endTime: Date

}