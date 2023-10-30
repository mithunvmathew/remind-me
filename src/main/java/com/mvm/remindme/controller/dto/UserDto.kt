package com.mvm.remindme.controller.dto

data class UserDto(
    val email: String,
    val password: String,
    val phone: String?,
    val morningReminder: Boolean = true,
    val eveningReminder: Boolean = true,
    val isRemindByEmail: Boolean = true,
    val isRemindByWhatsapp: Boolean = true,
    val isVerified: Boolean?
)
