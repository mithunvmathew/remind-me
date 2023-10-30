package com.mvm.remindme.service

import com.mvm.remindme.controller.dto.UserDto
import com.mvm.remindme.error.BadRequestException.*
import com.mvm.remindme.repository.UserRepository
import com.mvm.remindme.repository.model.User
import com.mvm.remindme.service.mapper.UserDataMapper
import com.mvm.remindme.utility.getAuthenticatedUser
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userDataMapper: UserDataMapper,
    private val reminderService: ReminderService,
) {
    fun findById(username: String): User = userRepository.findById(username)
        .orElseThrow { UserNotFoundException("User not found") }

    fun save(request: UserDto): User = when (userRepository.findById(request.email).isPresent) {
        true -> throw EmailAlreadyExistException("Email already exist. Please try with another email")
        false -> userRepository.save(userDataMapper.mapToModel(request))
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