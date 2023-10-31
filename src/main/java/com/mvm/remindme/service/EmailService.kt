package com.mvm.remindme.service

import com.mvm.remindme.repository.UserRepository
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
    private val htmlTemplateRenderService: HtmlTemplateRenderService,
    private val userRepository: UserRepository,
) {
    fun sendTextEmail(recipientEmail: String, subject: String, activityTime: LocalDateTime) {
        if (isLegallyAllowedToSendEmail(recipientEmail)) {
            val htmlContent = htmlTemplateRenderService.renderCustomReminderEmail(
                subject = subject,
                activityTime = activityTime
            )
            val message = SimpleMailMessage()
            message.subject = subject
            message.text = htmlContent
            message.setTo(recipientEmail)
            emailSender.send(message)
        } else {
            println("user $recipientEmail is unsubscribed.")
        }


    }

    @Throws(MessagingException::class)
    fun sendCustomReminderHtmlEmail(to: String, subject: String, activityTime: LocalDateTime) {
        if (isLegallyAllowedToSendEmail(to)) {
            val message: MimeMessage = emailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true)
            val htmlContent = htmlTemplateRenderService.renderCustomReminderEmail(
                subject = subject,
                activityTime = activityTime
            )

            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(htmlContent, true) // true indicates HTML content

            // Send the email
            emailSender.send(message)
        } else {
            println("user $to is unsubscribed.")
        }
    }

    @Throws(MessagingException::class)
    fun sendEmailVerificationHtmlEmail(to: String, verificationUrl: String, unsubscribeUrl: String) {
        if (isLegallyAllowedToSendEmail(to)) {
            val message: MimeMessage = emailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true)
            val htmlContent =
                htmlTemplateRenderService.renderEmailVerificationEmail(
                    verificationUrl = verificationUrl, unsubscribeUrl = unsubscribeUrl)

            helper.setTo(to)
            helper.setSubject("RemindMe: Email Verification")
            helper.setText(htmlContent, true) // true indicates HTML content

            // Send the email
            emailSender.send(message)
        } else {
            println("user $to is unsubscribed.")
        }
    }

    private fun isLegallyAllowedToSendEmail(recipientEmail: String) = userRepository.findById(
        recipientEmail
    ).get().isUnsubscribed != true


}