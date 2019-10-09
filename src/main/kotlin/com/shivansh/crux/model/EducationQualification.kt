package com.shivansh.crux.model

import javax.persistence.*

@Entity
@Table(name = "education_qualification")
class EducationQualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var degree: String
    lateinit var course: String
    lateinit var institute: String
    lateinit var grade: String
    var year: Int = 0

    @ManyToOne
    @JoinColumn(name = "userId")
    lateinit var user: User
}