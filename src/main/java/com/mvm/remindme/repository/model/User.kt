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

    @OneToMany(mappedBy="user")
    val reminderSet : Set<Reminder>? = null
) {

}

