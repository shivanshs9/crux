package com.shivansh.crux.model

import javax.persistence.*

@Entity
@Table(name = "coding_language")
class CodingLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var language: String
    lateinit var compiler: String
}