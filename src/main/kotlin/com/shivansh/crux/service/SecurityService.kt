package com.shivansh.crux.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

interface ISecurityService {
    fun findLoggedInUsername(): String?
    fun autoLogin(username: String, password: String): Boolean
    fun logout(): Boolean
}

@Service
class SecurityService: ISecurityService {
    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userDetailsService: UserDetailsService

    private val logger = LoggerFactory.getLogger(SecurityService::class.java)

    private fun findLoggedInUserDetails(): UserDetails? = (SecurityContextHolder.getContext().authentication.principal as? UserDetails)

    override fun findLoggedInUsername(): String? = findLoggedInUserDetails()?.username

    override fun logout(): Boolean {
        SecurityContextHolder.getContext().authentication = null
        return true
    }

    @Throws(AuthenticationException::class, UsernameNotFoundException::class)
    override fun autoLogin(username: String, password: String): Boolean {
        val userDetails = userDetailsService.loadUserByUsername(username)

        val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, password, userDetails.authorities)
        val authentication = authenticationManager.authenticate(authenticationToken)

        if (authentication.isAuthenticated) {
            SecurityContextHolder.getContext().authentication = authentication
            logger.debug("Auto login $username successfully!")
        }
        return authentication.isAuthenticated
    }
}
