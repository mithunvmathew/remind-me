package com.mvm.remindme.config

import io.pebbletemplates.pebble.PebbleEngine
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*


@Configuration
@EnableConfigurationProperties(EmailServerProperties::class)
class EmailSenderConfig(
    private val emailServerProperties: EmailServerProperties
) {

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = emailServerProperties.host
        mailSender.port = emailServerProperties.port.toInt()
        mailSender.username = emailServerProperties.username
        mailSender.password = emailServerProperties.password
        configureJavaMailProperties(mailSender.javaMailProperties)
        return mailSender
    }

    private fun configureJavaMailProperties(properties: Properties) {
        properties["mail.transport.protocol"] = "smtp"
        properties["mail.smtp.auth"] = true
        properties["mail.smtp.starttls.enable"] = true
        properties["mail.debug"] = true
    }

    @Bean
    fun pebbleEngine(): PebbleEngine = PebbleEngine.Builder().build()

}