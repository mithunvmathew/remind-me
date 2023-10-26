package com.mvm.remindme.controller.dto

import java.util.UUID

data class ReminderResponse(
    val id: UUID,
    val reminder: ReminderDto

) {
}