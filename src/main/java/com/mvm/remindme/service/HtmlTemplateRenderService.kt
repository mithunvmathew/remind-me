package com.mvm.remindme.service

import io.pebbletemplates.pebble.PebbleEngine
import io.pebbletemplates.pebble.template.PebbleTemplate
import org.springframework.stereotype.Service
import java.io.StringWriter
import java.io.Writer
import java.time.LocalDateTime

@Service
class HtmlTemplateRenderService(
    private val pebbileEngine: PebbleEngine
) {

    fun renderCustomReminderEmail(subject: String, activityTime: LocalDateTime): String {

        val compiledTemplate: PebbleTemplate = pebbileEngine.getTemplate("templates/custom-reminder_old.html")

        val context: MutableMap<String, Any> = HashMap()
        context["subject"] = subject
        context["actionDate"] = getDate(activityTime)
        context["actionTime"] = getTime(activityTime)

        val writer: Writer = StringWriter()
        compiledTemplate.evaluate(writer, context)

        return writer.toString()
    }

    fun renderEmailVerificationEmail(verificationUrl: String, unsubscribeUrl: String): String {

        val compiledTemplate: PebbleTemplate = pebbileEngine.getTemplate("templates/email-verification.html")

        val context: MutableMap<String, Any> = HashMap()
        context["verificationUrl"] = verificationUrl
        context["unsubscribeUrl"] = unsubscribeUrl

        val writer: Writer = StringWriter()
        compiledTemplate.evaluate(writer, context)

        return writer.toString()
    }



    private fun getTime(activityTime: LocalDateTime) =
        activityTime.hour.toString() + "." + activityTime.minute + "." + activityTime.second

    private fun getDate(activityTime: LocalDateTime): String =
        activityTime.year.toString() + "." + activityTime.month.value.toString() + "." + activityTime.dayOfMonth
}