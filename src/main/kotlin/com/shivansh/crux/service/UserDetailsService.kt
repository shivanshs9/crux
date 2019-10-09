package com.shivansh.crux.service

import com.shivansh.crux.model.BusinessMember
import com.shivansh.crux.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsService: UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var businessService: IBusinessService

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
        return user?.let {
            val authorities = mutableListOf<GrantedAuthority>()
            val businesses = businessService.findByUser(it)
            authorities.add(REGULAR_USER)
            businesses.map { authorities.add(if (it.position == BusinessMember.POSITION.Owner) BUSINESS_OWNER else BUSINESS_EMPLOYEE) }

            User(it.username, it.password, authorities)
        } ?: throw UsernameNotFoundException(username)
    }

    companion object {
        val REGULAR_USER = SimpleGrantedAuthority("regular")
        val BUSINESS_EMPLOYEE = SimpleGrantedAuthority("business-employee")
        val BUSINESS_OWNER = SimpleGrantedAuthority("business-owner")
    }
}