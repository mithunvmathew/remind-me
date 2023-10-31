package com.mvm.remindme.repository

import com.mvm.remindme.repository.model.User
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository: CrudRepository<User, String> {

    override fun findById(username: String): Optional<User>

    fun findByVerificationCode(verificationCode: String): Optional<User>

    @Modifying
    @Transactional
    @Query("UPDATE User u " +
            "SET u.isVerified = :isVerified " +
            "WHERE u.userName = :username")
    fun updateUserVerify(@Param("isVerified") isVerified: Boolean?,@Param("username") username: String)

}
