package com.shivansh.crux.util

interface IRequestData {
    fun clean()
    fun validate() {
        clean()
    }

    fun isValid(): Boolean
}

open class RequestData : IRequestData, BaseResponseData {
    override val errors: MutableMap<String, String> = mutableMapOf()

    override fun clean() {}

    fun invalidField(field: String, error: String) {
        errors[field] = error
    }

    override fun isValid(): Boolean = errors.isEmpty()
}
