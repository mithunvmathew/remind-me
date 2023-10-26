package com.mvm.remindme.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("email")
class EmailServerProperties (
    val username: String,
    val password: String,
    val host: String,
    val port: String,
)