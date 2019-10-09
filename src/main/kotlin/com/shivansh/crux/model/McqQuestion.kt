package com.shivansh.crux.model

import javax.persistence.*

@Entity
@Table(name = "mcq_question")
class McqQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var title: String
    lateinit var description: String
    var marks: Int = 0

    @ManyToOne
    @JoinColumn(name = "testId")
    lateinit var test: Test

    @ManyToOne
    @JoinColumn(name = "problemSetterId")
    lateinit var problemSetter: ProblemSetter
}