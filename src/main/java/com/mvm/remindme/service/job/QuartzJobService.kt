package com.mvm.remindme.service.job

import com.mvm.remindme.repository.model.Reminder
import org.quartz.*
import org.springframework.stereotype.Component
import java.util.*


@Component
class QuartzJobService(
    private val scheduler: Scheduler
) {

    fun createCustomReminderScheduleJob(reminder: Reminder) {
        val jobDataMap = JobDataMap()
        jobDataMap.put("message", reminder.subject)
        jobDataMap.put("recipient", reminder.user.userName)
        jobDataMap["activityTime"] = reminder.activityTime

        val jobDetail = JobBuilder.newJob()
            .withIdentity(UUID.randomUUID().toString())
            .setJobData(jobDataMap)
            .withDescription("custom reminder job")
            .ofType(ReminderExecutionJob::class.java)
            .storeDurably()
            .build()
        val trigger: Trigger = TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .startAt(Date.from(reminder.reminderTime))
            .withDescription("custom reminder job trigger.")
            .build()
        scheduler.scheduleJob(jobDetail, trigger);
        println("custom reminder scheduled :${reminder.subject}")

    }
}