package com.mvm.remindme.controller

import com.mvm.remindme.error.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpServerErrorException.InternalServerError

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(InternalServerError::class)
    fun hasInternalServerWebException(): ResponseEntity<CustomError> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomError(
           message = "Error from Backend service"
        ))
    }

    @ExceptionHandler(BadRequestException::class)
    fun hasBadRequestException(
        badRequestException: BadRequestException): ResponseEntity<CustomError> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomError(
            message = badRequestException.message
        ))
    }

}

data class CustomError(
    val message: String?
)