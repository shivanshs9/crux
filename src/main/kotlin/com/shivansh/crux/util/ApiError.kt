package com.shivansh.crux.util

interface BaseErrorResponse: BaseResponseData

data class ApiError(override val errors: Map<String, String>?) : BaseErrorResponse