package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "business_member")
class BusinessMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var joinedTime: Date

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    lateinit var position: POSITION

    @ManyToOne
    @JoinColumn(name = "businessId")
    lateinit var business: Business

    @ManyToOne
    @JoinColumn(name = "userId")
    lateinit var user: User

    enum class POSITION {
        Owner, Employee
    }
}