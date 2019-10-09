package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "business")
class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var name: String
    var logo: String? = null
    var createdTime: Date? = null
}