package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var username: String
    var firstName: String = ""
    var lastName: String? = null

    lateinit var password: String

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Male','Female')")
    lateinit var gender: GENDER

    var country: String = "IN"
    var state: String? = null
    var city: String? = null
    var address: String? = null
    lateinit var DOB: Date
    lateinit var joinedTime: Date

    enum class GENDER {
        Male, Female;
    }

    fun getName(): String = "$firstName ${lastName ?: "($username)"}"
}