package com.shivansh.crux.service

import com.shivansh.crux.model.User
import org.springframework.stereotype.Service
import com.shivansh.crux.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

interface IUserService {
    fun save(user: User)
    fun findByUsername(username: String): User?
}

@Service
class UserService: IUserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    override fun save(user: User) {
        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(user)
    }

    override fun findByUsername(username: String): User? = userRepository.findByUsername(username)
}