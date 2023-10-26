package com.mvm.remindme.service.mapper

import com.mvm.remindme.controller.dto.ReminderDto
import com.mvm.remindme.repository.model.Reminder
import com.mvm.remindme.repository.model.User
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReminderServiceMapper {

    fun mapToModel(reminderDto: ReminderDto, user: User) : Reminder = Reminder(
        id = reminderDto.id ?: UUID.randomUUID(),
        subject = reminderDto.subject,
        activityTime = reminderDto.timeToActive,
        reminderTime = reminderDto.timeToRemind,
        isActive = reminderDto.isActive ?: true,
        morningReminder = reminderDto.morningReminder,
        eveningReminder = reminderDto.eveningReminder,
        user = user
    )

    fun mapToDto(reminder: Reminder) : ReminderDto = ReminderDto(
        id = reminder.id,
        subject = reminder.subject,
        timeToActive = reminder.activityTime,
        timeToRemind = reminder.reminderTime,
        isActive = reminder.isActive ?: true,
        morningReminder = reminder.morningReminder,
        eveningReminder = reminder.eveningReminder,
    )
}