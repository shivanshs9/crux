package com.shivansh.crux

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidDataException(val errors: Map<String, String>) : Exception("Invalid data provided.")

@ResponseStatus(HttpStatus.FORBIDDEN)
class InvalidRoleProvidedException(msg: String) : Exception(msg)
