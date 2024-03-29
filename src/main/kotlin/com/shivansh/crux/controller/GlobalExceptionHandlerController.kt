package com.shivansh.crux.controller

import com.shivansh.crux.DataNotFoundException
import com.shivansh.crux.InvalidDataException
import com.shivansh.crux.InvalidRoleProvidedException
import com.shivansh.crux.util.ApiError
import com.shivansh.crux.util.BaseErrorResponse
import com.shivansh.crux.util.RequestData
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.util.WebUtils
import javax.servlet.http.HttpServletRequest



@ControllerAdvice
class GlobalExceptionHandlerController {
    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFound(ex: Exception, request: WebRequest): ResponseEntity<BaseErrorResponse> {
        val data = RequestData().apply { invalidField("username", "Username '${ex.message}' not found.") }
        return handleExceptionInternal(ex, ApiError(data.errors), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: Exception, request: WebRequest): ResponseEntity<BaseErrorResponse> {
        val data = RequestData().apply { invalidField("password", ex.message ?: "Invalid credentials provided") }
        return handleExceptionInternal(ex, ApiError(data.errors), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(InvalidDataException::class)
    fun handleInvalidDataProvided(ex: InvalidDataException, request: WebRequest): ResponseEntity<BaseErrorResponse> {
        return handleExceptionInternal(ex, ApiError(ex.errors), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(InvalidRoleProvidedException::class)
    fun handleInvalidRoleProvided(ex: InvalidRoleProvidedException, request: WebRequest): ResponseEntity<BaseErrorResponse> {
        val data = RequestData().apply { invalidField("role", ex.message ?: "Invalid role provided") }
        return handleExceptionInternal(ex, ApiError(data.errors), HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }

    @ExceptionHandler(DataNotFoundException::class)
    fun handleDataNotFoundException(ex: DataNotFoundException, request: WebRequest): ResponseEntity<BaseErrorResponse> {
        val data = RequestData().apply { invalidField("data", ex.message ?: "Data not found") }
        return handleExceptionInternal(ex, ApiError(data.errors), HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    /**
     * A single place to customize the response body of all Exception types.
     *
     *
     * The default implementation sets the [WebUtils.ERROR_EXCEPTION_ATTRIBUTE]
     * request attribute and creates a [ResponseEntity] from the given
     * body, headers, and status.
     *
     * @param ex The exception
     * @param body The body for the response
     * @param headers The headers for the response
     * @param status The response status
     * @param request The current request
     */
    protected fun handleExceptionInternal(ex: Exception, body: ApiError?,
                                          headers: HttpHeaders, status: HttpStatus,
                                          request: WebRequest): ResponseEntity<BaseErrorResponse> {
        if (HttpStatus.INTERNAL_SERVER_ERROR == status) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST)
        }
        return ResponseEntity(body, headers, status)
    }

    private fun isAjax(request: HttpServletRequest): Boolean {
        val requestedWithHeader = request.getHeader("X-Requested-With")
        return "XMLHttpRequest" == requestedWithHeader
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerController::class.java)
    }
}