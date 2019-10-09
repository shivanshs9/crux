package com.shivansh.crux.model

import javax.persistence.*

@Entity
@Table(name = "mcq_option")
class McqOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var statement: String
    var isCorrect: Boolean = false

    @ManyToOne
    @JoinColumn(name = "questionId")
    lateinit var question: McqQuestion
}