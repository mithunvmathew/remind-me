package com.mvm.remindme.error

sealed class BadRequestException(
    override val message: String
): Throwable() {
    class EmailAlreadyExistException(message: String) : BadRequestException(
        message = message
    )
    class PasswordNotMatchingException(message: String): BadRequestException(
        message = message
    )

    class UserNotFoundException(message: String): BadRequestException(
        message = message
    )

    class ReminderNotFoundException(message: String): BadRequestException(
        message = message
    )

    class InvalidDataException(message: String): BadRequestException(
        message = message
    )
}