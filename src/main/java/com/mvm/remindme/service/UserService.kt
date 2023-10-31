package com.mvm.remindme.service

import com.mvm.remindme.controller.dto.UserDto
import com.mvm.remindme.error.BadRequestException.*
import com.mvm.remindme.repository.UserRepository
import com.mvm.remindme.repository.model.User
import com.mvm.remindme.service.mapper.UserDataMapper
import com.mvm.remindme.utility.getAuthenticatedUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userDataMapper: UserDataMapper,
    private val reminderService: ReminderService,
    private val emailService: EmailService,
    @Value("\${base.url}") var baseUrl: String,
) {
    fun findById(username: String): User = userRepository.findById(username)
        .orElseThrow { UserNotFoundException("User not found") }

    fun save(request: UserDto): User = when (userRepository.findById(request.email).isPresent) {
        true -> throw EmailAlreadyExistException("Email already exist. Please try with another email")
        false -> {
            val user = userRepository.save(userDataMapper.mapToModel(request))
            emailService.sendEmailVerificationHtmlEmail(
                verificationUrl = baseUrl + "auth/email/verify/" + user.verificationCode,
                to = user.userName
            )
            user
        }
    }

    fun verifyEmail(verificationToken: String) {
        val user = userRepository.findByVerificationCode(verificationToken)
            .orElseThrow { InvalidDataException("Invalid verification code") }
        userRepository.updateUserVerify(isVerified = true, username = user.userName)
    }

    fun findLoggedInUser() = userDataMapper.mapToDto(
        userRepository.findById(getAuthenticatedUser())
            .orElseThrow { UserNotFoundException("User not found") }
    )

    fun updateUser(userDto: UserDto): UserDto {
        val username = getAuthenticatedUser()
        if (username != userDto.email) {
            throw IllegalDataAccessException("Logged in user not authorised to process the update request")
        }
        return userDataMapper.mapToDto(userRepository.save(userDataMapper.mapToModel(userDto)))
    }

    fun deleteUser() {
        reminderService.deleteAllReminders()
        userRepository.deleteById(getAuthenticatedUser())
    }

}