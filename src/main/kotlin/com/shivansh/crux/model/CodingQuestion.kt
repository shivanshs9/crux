package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "coding_question")
class CodingQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var title: String
    lateinit var problemStatement: String
    var marks: Int = 0
    lateinit var createdTime: Date

    @ManyToOne
    @JoinColumn(name = "testId")
    lateinit var test: Test

    @ManyToOne
    @JoinColumn(name = "problemSetterId")
    lateinit var problemSetter: ProblemSetter
}