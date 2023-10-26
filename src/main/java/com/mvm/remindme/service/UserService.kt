package com.mvm.remindme.service

import com.mvm.remindme.controller.dto.RegisterRequest
import com.mvm.remindme.error.BadRequestException.UserNotFoundException
import com.mvm.remindme.error.BadRequestException.EmailAlreadyExistException
import com.mvm.remindme.repository.model.User
import com.mvm.remindme.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun findById(username: String): User = userRepository.findById(username)
        .orElseThrow{UserNotFoundException("User not found")}


    fun save(request: RegisterRequest): User = when(userRepository.findById(request.email).isPresent) {
        true -> throw EmailAlreadyExistException("Email already exist. Please try with another email")
        false -> userRepository.save(
            User(
            userName = request.email,
            password = BCrypt.hashpw(request.password, BCrypt.gensalt(10)))
        )
    }
}