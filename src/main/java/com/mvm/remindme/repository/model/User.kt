package com.mvm.remindme.repository.model

import com.mvm.remindme.config.NoArgConstructor
import jakarta.persistence.*


@NoArgConstructor
@Entity
@Table(name = "user_data")
class User(
    @Id
    @Column(name = "username") val userName: String,
    @Column val password: String,

    @Column val phone: String?,

    @Column(name = "morning_reminder")
    val morningReminder: Boolean,

    @Column(name = "evening_reminder")
    val eveningReminder: Boolean,

    @Column(name = "remind_by_email")
    val isRemindByEmail: Boolean,

    @Column(name = "remind_by_whatsapp")
    val isRemindByWhatsapp: Boolean,

    @Column(name = "is_verified")
    val isVerified: Boolean?,

    @Column(name = "verification_code")
    val verificationCode: String? = null,

    @OneToMany(mappedBy="user", cascade = [CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REMOVE])
    val reminderSet : Set<Reminder>? = null
)

