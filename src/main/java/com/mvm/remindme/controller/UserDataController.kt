package com.mvm.remindme.controller

import com.mvm.remindme.controller.dto.InfoDto
import com.mvm.remindme.controller.dto.UserDto
import com.mvm.remindme.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserDataController(
    private val userService: UserService,
) {

    @GetMapping
    fun getUserData(): UserDto {
        return userService.findLoggedInUser()
    }

    @PutMapping
    fun updateUser(@RequestBody userDto: UserDto): UserDto {
        return userService.updateUser(userDto)
    }

    @DeleteMapping
    fun deleteUser(): InfoDto {
        userService.deleteUser()
        return InfoDto(message = "User deleted successfully")
    }
}