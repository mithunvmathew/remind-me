package com.mvm.remindme.utility

import com.mvm.remindme.repository.model.User
import org.springframework.security.core.context.SecurityContextHolder

fun getAuthenticatedUser():String =
    (SecurityContextHolder.getContext().authentication.principal as User).userName