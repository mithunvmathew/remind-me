package com.mvm.remindme.service.job

import com.mvm.remindme.service.EmailService
import org.quartz.Job
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReminderExecutionJob(
    private val emailService: EmailService
): Job {
    override fun execute(context: JobExecutionContext?) {
        val jobDataMap: JobDataMap = context?.mergedJobDataMap ?: throw Exception("")
        val message = jobDataMap["message"].toString()
        val recipient = jobDataMap["recipient"].toString()
        val activityTime = jobDataMap["activityTime"]

        emailService.sendHtmlEmail(to = recipient, subject = message, activityTime = activityTime as LocalDateTime   )
        println("Email sending job started with message $message")
    }
}