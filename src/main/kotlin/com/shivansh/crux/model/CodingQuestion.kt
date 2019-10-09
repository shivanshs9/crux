package com.shivansh.crux.model

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

    @ManyToOne
    @JoinColumn(name = "testId")
    lateinit var test: Test

    @ManyToOne
    @JoinColumn(name = "problemSetter")
    lateinit var problemSetter: ProblemSetter
}