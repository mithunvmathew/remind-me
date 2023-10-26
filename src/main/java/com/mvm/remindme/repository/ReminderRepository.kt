package com.mvm.remindme.repository

import com.mvm.remindme.repository.model.Reminder
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReminderRepository: CrudRepository<Reminder, UUID> {

    @Query(value ="select * from reminders where user_fk =:userName", nativeQuery = true)
    fun findAllByUser(userName: String): List<Reminder>
}