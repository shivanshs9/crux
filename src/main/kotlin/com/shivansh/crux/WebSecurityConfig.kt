package com.shivansh.crux

import com.shivansh.crux.service.UserDetailsService.Companion.BUSINESS_EMPLOYEE
import com.shivansh.crux.service.UserDetailsService.Companion.BUSINESS_OWNER
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun customAuthenticationManager(): AuthenticationManager = authenticationManager()

    override fun configure(http: HttpSecurity?) {
        http?.let {
            it.authorizeRequests()
                    .antMatchers("/", "/static/**", "/auth/**").permitAll()
                    .antMatchers("/business/admin/**").hasAuthority(BUSINESS_OWNER.authority)
                    .antMatchers("/business/work/**").hasAuthority(BUSINESS_EMPLOYEE.authority)
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/?login")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll()
        }
    }

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder())
    }
}