package com.mvm.remindme.controller

import com.mvm.remindme.controller.dto.InfoDto
import com.mvm.remindme.controller.dto.ReminderDto
import com.mvm.remindme.service.ReminderService
import org.springframework.web.bind.annotation.*
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@RequestMapping("/api/reminder")
class ReminderController(
    private val reminderService: ReminderService
) {

    @PostMapping("/create")
    fun create(@RequestBody reminderDto: ReminderDto): ReminderDto {
        return reminderService.createReminder(reminderDto)
    }

    @GetMapping("/{reminderId}")
    fun getReminder(@PathVariable("reminderId") reminderId: UUID): ReminderDto {
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return reminderService.getReminder(reminderId)
    }

    @GetMapping
    fun getAllReminder(
        @RequestParam(
            value = "isActive",
            required = false
        ) isActive: Boolean = true
    ): List<ReminderDto> {
        return reminderService.getAllReminders(isActive)
    }

   /* @PutMapping
    fun updateReminder(@RequestBody reminderDto: ReminderDto): ReminderDto {
        return reminderService.createReminder(reminderDto)
    }*/


    @DeleteMapping("/{reminderId}")
    fun deleteReminder(@PathVariable("reminderId") reminderId: UUID): InfoDto {
        reminderService.deleteReminder(reminderId)
        return InfoDto(message = "Reminder $reminderId deleted successfully")
    }
}
