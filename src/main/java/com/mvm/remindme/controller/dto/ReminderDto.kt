package com.mvm.remindme.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.security.auth.Subject

data class ReminderDto(
    val id: UUID?,
    val subject: String,
    val timeToActive: LocalDateTime,
    val timeToRemind: Instant,
    val morningReminder: Boolean,
    val eveningReminder: Boolean,
    val isActive: Boolean?
)
