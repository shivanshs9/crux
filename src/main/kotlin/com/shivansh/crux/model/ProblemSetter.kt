package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "problem_setter")
class ProblemSetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    var isCreator: Boolean = false
    lateinit var joinedTime: Date

    @ManyToOne
    @JoinColumn(name = "testId")
    lateinit var test: Test

    @ManyToOne
    @JoinColumn(name = "businessMemberId")
    lateinit var businessMember: BusinessMember
}