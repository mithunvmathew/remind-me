package com.mvm.remindme.service

import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class EmailService(
    private val emailSender: JavaMailSender,
    private val htmlTemplateRenderService: HtmlTemplateRenderService
)
    {
    fun sendTextEmail(recipientEmail: String, subject: String, activityTime: LocalDateTime) {

        val htmlContent = htmlTemplateRenderService.renderCustomReminderEmail(subject = subject,
            activityTime = activityTime)
        val message = SimpleMailMessage()
        message.subject = subject
        message.text = htmlContent
        message
        message.setTo(recipientEmail)

        emailSender.send(message)

    }

        @Throws(MessagingException::class)
        fun sendHtmlEmail(to: String, subject: String, activityTime: LocalDateTime) {
            val message: MimeMessage = emailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true)
            val htmlContent = htmlTemplateRenderService.renderCustomReminderEmail(subject = subject,
                activityTime = activityTime)

            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(htmlContent, true) // true indicates HTML content

            // Send the email
            emailSender.send(message)
        }
}