package com.shivansh.crux.util

data class ApiError(override val errors: Map<String, String>?) : BaseResponseData