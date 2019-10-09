package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "account")
class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('EMAIL', 'PHONE')")
    lateinit var providerType: ProviderType

    lateinit var createdTime: Date
    lateinit var providerId: String

    @ManyToOne
    @JoinColumn(name = "userId")
    lateinit var user: User

    enum class ProviderType {
        EMAIL, PHONE
    }
}