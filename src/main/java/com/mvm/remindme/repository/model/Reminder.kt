package com.mvm.remindme.repository.model

import com.mvm.remindme.config.NoArgConstructor
import com.mvm.remindme.repository.model.User
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@NoArgConstructor
@Entity
@Table(name = "reminders")
data class Reminder(

    @Id
    @Column
    val id: UUID,

    @Column
    val subject: String,

    @Column(name = "activity_time")
    val activityTime: LocalDateTime,

    @Column(name = "active")
    val isActive: Boolean,

    @Column(name = "morning_reminder")
    val morningReminder: Boolean,

    @Column(name = "evening_reminder")
    val eveningReminder: Boolean,

    @Column(name = "reminder_time")
    val reminderTime: Instant,

    @ManyToOne
    @JoinColumn(name="user_fk")
    val user: User

)
