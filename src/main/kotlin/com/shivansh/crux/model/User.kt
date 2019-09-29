package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "User")
class User {
    @Id
    @GeneratedValue
    var id: Long = 0

    lateinit var username: String
    var firstName: String = ""
    var lastName: String? = null

    lateinit var password: String
    @Transient private var passwordConfirm: String? = null

    private var country: String = "IN"
    private var state: String? = null
    private var city: String? = null
    private var pincode: Char = ' '
    private var address: String? = null
    private var DOB: Date? = null
    private var joinedTime: Date? = null

}