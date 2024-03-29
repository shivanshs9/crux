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

    @Column(name = "hq_country")
    var hqCountry: String = "IN"
    @Column(name = "hq_state")
    var hqState: String? = null
    @Column(name = "hq_city")
    var hqCity: String? = null
    @Column(name = "hq_address")
    var hqAddress: String? = null

    var category: String? = null
}