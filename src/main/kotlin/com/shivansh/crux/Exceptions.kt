package com.shivansh.crux

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidDataException(val errors: Map<String, String>) : Exception("Invalid data provided.") {
    constructor(msg: String): this(mapOf("detail" to msg))
}

@ResponseStatus(HttpStatus.FORBIDDEN)
open class InvalidRoleProvidedException(msg: String) : Exception(msg)

@ResponseStatus(HttpStatus.NOT_FOUND)
class DataNotFoundException(msg: String?): Exception(msg ?: "Data not found")

@ResponseStatus(HttpStatus.FORBIDDEN)
class TestMarkedOverException(): InvalidRoleProvidedException("Test marked over")