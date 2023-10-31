package com.mvm.remindme.service.mapper

import com.mvm.remindme.controller.dto.UserDto
import com.mvm.remindme.repository.model.User
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.Random
import java.util.UUID

@Component
class UserDataMapper {

    fun mapToDto(user: User): UserDto = UserDto(
        email = user.userName,
        password = user.password,
        phone = user.phone,
        morningReminder = user.morningReminder,
        eveningReminder = user.eveningReminder,
        isRemindByEmail = user.isRemindByEmail,
        isRemindByWhatsapp = user.isRemindByWhatsapp,
        isVerified = user.isVerified
    )

    fun mapToModel(userDto: UserDto): User = User(
        userName = userDto.email,
        password = BCrypt.hashpw(userDto.password, BCrypt.gensalt(10)),
        phone = userDto.phone,
        morningReminder = userDto.morningReminder,
        eveningReminder = userDto.eveningReminder,
        isRemindByEmail = userDto.isRemindByEmail,
        isRemindByWhatsapp = userDto.isRemindByWhatsapp,
        isVerified = userDto.isVerified,
        verificationCode = generateVerificationCode(30),
    )

    fun generateVerificationCode(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset[Random().nextInt(0, charset.length)] }
            .joinToString("")
    }
}