package com.shivansh.crux.model

import javax.persistence.*

@Entity
@Table(name = "test_case")
class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var input: String
    lateinit var output: String
    var score: Int = 0

    @ManyToOne
    @JoinColumn(name = "questionId")
    lateinit var question: CodingQuestion
}