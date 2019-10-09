package com.shivansh.crux.repository

import com.shivansh.crux.model.Business
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BusinessRepository : CrudRepository<Business, Long> {
    fun findByName(name: String): Business?
}