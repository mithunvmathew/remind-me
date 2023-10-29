package com.mvm.remindme.service

import com.mvm.remindme.controller.dto.ReminderDto
import com.mvm.remindme.error.BadRequestException.ReminderNotFoundException
import com.mvm.remindme.error.BadRequestException.InvalidDataException
import com.mvm.remindme.error.BadRequestException.UserNotFoundException
import com.mvm.remindme.repository.ReminderRepository
import com.mvm.remindme.repository.UserRepository
import com.mvm.remindme.service.job.QuartzJobService
import com.mvm.remindme.service.mapper.ReminderServiceMapper
import com.mvm.remindme.utility.getAuthenticatedUser
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


@Service
class ReminderService(
    private val reminderRepository: ReminderRepository,
    private val userRepository: UserRepository,
    private val reminderServiceMapper: ReminderServiceMapper,
    private val quartzJobService: QuartzJobService,
) {
    fun createReminder(reminderRequest: ReminderDto): ReminderDto {
        val userName = getAuthenticatedUser()
        val user = if (userRepository.findById(userName).isPresent) {
            userRepository.findById(userName).get()
        } else throw UserNotFoundException("User $userName not found")
        validateTime(reminderRequest)
        val reminder = reminderRepository.save(reminderServiceMapper.mapToModel(reminderRequest, user))
        quartzJobService.createCustomReminderScheduleJob(reminder)
        return reminderServiceMapper.mapToDto(reminder)
    }

    fun getReminder(reminderId: UUID) = reminderServiceMapper.mapToDto(
        reminderRepository.findById(reminderId).orElseThrow {
            ReminderNotFoundException("Reminder with id : $reminderId not found")
        }
    )

    fun getAllReminders(isActive: Boolean): List<ReminderDto> {
        val user = getAuthenticatedUser()
        return reminderRepository.findAllByUser(user).filter { it.isActive == isActive }.map {
            reminderServiceMapper.mapToDto(it)
        }
    }

    fun deleteReminder(reminderId: UUID) {
        quartzJobService.deleteCustomScheduledJob(reminderId)
        reminderRepository.deleteById(reminderId)
    }

    private fun validateTime(reminderDto: ReminderDto) {
        if(reminderDto.timeToActive.toInstant(ZoneOffset.UTC) < Instant.now()) {
            throw InvalidDataException("Time to active must be a future time")
        }

        if(reminderDto.timeToRemind < Instant.now()) {
            throw InvalidDataException("Time to remind must be a future time")
        }

    }

}