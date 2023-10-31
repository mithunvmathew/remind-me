package com.mvm.remindme.service

import com.mvm.remindme.repository.UserRepository
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${base.url}") var baseUrl: String,
) {
    fun sendTextEmail(recipientEmail: String, subject: String, activityTime: LocalDateTime) {
        val user = userRepository.findById(recipientEmail).get()
        if (user.isUnsubscribed == false) {
            val unsubscribeUrl = baseUrl + "auth/email/unsubscribe/" + user.verificationCode
            val htmlContent = htmlTemplateRenderService.renderCustomReminderEmail(
                subject = subject,
                activityTime = activityTime,
                unsubscribeUrl = unsubscribeUrl
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
        val user = userRepository.findById(to).get()
        if (user.isUnsubscribed == false) {
            val unsubscribeUrl = baseUrl + "auth/email/unsubscribe/" + user.verificationCode
            val message: MimeMessage = emailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true)
            val htmlContent = htmlTemplateRenderService.renderCustomReminderEmail(
                subject = subject,
                activityTime = activityTime,
                unsubscribeUrl = unsubscribeUrl
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
    fun sendEmailVerificationHtmlEmail(to: String) {
        val user = userRepository.findById(to).get()
        if (user.isUnsubscribed == false) {
            val verificationUrl = baseUrl + "auth/email/verify/" + user.verificationCode
            val unsubscribeUrl = baseUrl + "auth/email/unsubscribe/" + user.verificationCode
            val message: MimeMessage = emailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true)
            val htmlContent =
                htmlTemplateRenderService.renderEmailVerificationEmail(
                    verificationUrl = verificationUrl, unsubscribeUrl = unsubscribeUrl
                )

            helper.setTo(to)
            helper.setSubject("RemindMe: Email Verification")
            helper.setText(htmlContent, true) // true indicates HTML content

            // Send the email
            emailSender.send(message)
        } else {
            println("user $to is unsubscribed.")
        }
    }

}