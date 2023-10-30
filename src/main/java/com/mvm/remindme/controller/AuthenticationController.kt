package com.mvm.remindme.controller

import com.mvm.remindme.controller.dto.LoginRequest
import com.mvm.remindme.controller.dto.LoginResponse
import com.mvm.remindme.controller.dto.UserDto
import com.mvm.remindme.error.BadRequestException
import com.mvm.remindme.service.TokenService
import com.mvm.remindme.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val tokenService: TokenService,
    private val userService: UserService,

    ) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        val user = userService.findById(loginRequest.email)

        if (!tokenService.checkPassword(loginRequest.password.trim(), user.password.trim())) {
            throw BadRequestException.PasswordNotMatchingException("Password not matching")
        }
        return LoginResponse(
            accessToken = tokenService.createToken(user),
        )
    }

    @PostMapping("/register")
    fun register(@RequestBody userDto: UserDto): LoginResponse {
            val savedUser = userService.save(userDto)
            return LoginResponse(
                accessToken = tokenService.createToken(savedUser))
    }
}