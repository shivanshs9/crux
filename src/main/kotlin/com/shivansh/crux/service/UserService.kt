package com.shivansh.crux.service

import com.shivansh.crux.controller.RegisterData
import com.shivansh.crux.model.User
import com.shivansh.crux.model.UserAccount
import com.shivansh.crux.repository.UserAccountRepository
import com.shivansh.crux.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface IUserService {
    fun save(user: User)
    fun findByUsername(username: String): User?
    fun createNewUser(data: RegisterData): User?
    fun findByEmail(email: String): User?
}

@Service
class UserService: IUserService {
    @Autowired
    private lateinit var userAccountRepository: UserAccountRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    override fun save(user: User) {
        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(user)
    }

    @Transactional
    override fun createNewUser(data: RegisterData): User? {
        val user = User().apply {
            username = data.username!!
            password = data.password!!
            firstName = data.firstName!!
            lastName = data.lastName
            gender = User.GENDER.valueOf(data.gender!!)
            joinedTime = Calendar.getInstance().time
        }
        val account = UserAccount().apply {
            this.user = user
            this.providerType = UserAccount.ProviderType.EMAIL
            this.providerId = data.email!!
            this.createdTime = Calendar.getInstance().time
        }
        save(user)
        userAccountRepository.save(account)
        return user
    }

    override fun findByEmail(email: String): User? {
        val accounts = userAccountRepository.findByEmail(email)
        return if (accounts.isNotEmpty()) {
            accounts[0].user
        } else null
    }

    override fun findByUsername(username: String): User? = userRepository.findByUsername(username)
}