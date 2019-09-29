package com.shivansh.crux.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

interface ISecurityService {
    fun findLoggedInUsername(): String?
    fun autoLogin(username: String, password: String)
}

@Service
class SecurityService: ISecurityService {
    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userDetailsService: UserDetailsService

    private val logger = LoggerFactory.getLogger(SecurityService::class.java)

    override fun findLoggedInUsername(): String? {
        val userDetails = SecurityContextHolder.getContext().authentication.details
        return (userDetails as? UserDetails)?.username
    }

    override fun autoLogin(username: String, password: String) {
        val userDetails = userDetailsService.loadUserByUsername(username)

        val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, password, userDetails.authorities)
        authenticationManager.authenticate(authenticationToken)

        if (authenticationToken.isAuthenticated) {
            SecurityContextHolder.getContext().authentication = authenticationToken
            logger.debug("Auto login $username successfully!")
        }
    }
}
