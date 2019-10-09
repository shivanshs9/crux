package com.shivansh.crux.repository

import com.shivansh.crux.model.UserAccount
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAccountRepository : CrudRepository<UserAccount, Long> {
    fun findByProviderTypeAndId(providerType: UserAccount.ProviderType, providerId: String): List<UserAccount>

    @Query("SELECT ua FROM UserAccount ua WHERE ua.providerId = ?1 AND ua.providerType = 'EMAIL'")
    fun findByEmail(email: String): List<UserAccount>
}
