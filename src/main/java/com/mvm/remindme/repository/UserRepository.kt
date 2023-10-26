package com.mvm.remindme.repository


import com.mvm.remindme.repository.model.Reminder
import com.mvm.remindme.repository.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository: CrudRepository<User, String> {

    override fun findById(username: String): Optional<User>



}
