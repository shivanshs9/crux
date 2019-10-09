package com.shivansh.crux.util

interface IRequestData {
    val errors: Map<String, String>

    fun clean() {}
    fun validate() {
        clean()
    }

    fun isValid(): Boolean
}

open class RequestData : IRequestData, BaseResponseData {
    override val errors: MutableMap<String, String> = mutableMapOf()

    fun invalidField(field: String, error: String) {
        errors[field] = error
    }

    override fun isValid(): Boolean = errors.isEmpty()
}
