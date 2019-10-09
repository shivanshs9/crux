package com.shivansh.crux.controller

import com.shivansh.crux.InvalidDataException
import com.shivansh.crux.service.IBusinessService
import com.shivansh.crux.service.IUserService
import com.shivansh.crux.util.BaseResponseData
import com.shivansh.crux.util.RequestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class AuthController : BaseController() {
    @Autowired
    private lateinit var userService: IUserService

    @Autowired
    private lateinit var businessService: IBusinessService

    @PostMapping("/login")
    fun login(@ModelAttribute data: LoginData, request: HttpServletRequest, response: HttpServletResponse): BaseResponseData {
        data.validate()
        return if (data.isValid()) {
            securityService.autoLogin(data.username!!, data.password!!)
            request.session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext())
            response.setHeader("Access-Control-Allow-Credentials", "true")
            data.redirectTo = "/"
            data
        } else throw InvalidDataException(data.errors)
    }

    @PostMapping("/register")
    fun register(@ModelAttribute data: RegisterData, request: WebRequest): BaseResponseData {
        data.validate()
        return if (data.isValid()) {
            if (!data.validUsername(userService)) data.invalidField("username", "Username already taken")
            if (!data.validEmail(userService)) data.invalidField("email", "Email already taken")
            if (data.businessName?.let { data.validBusiness(businessService) } == false) data.invalidField("business", "Business name already in-use")

            if (data.isValid()) {
                val user = userService.createNewUser(data)
                if (!data.businessName.isNullOrBlank()) businessService.createNewBusiness(data.businessName!!, user!!)
                data
            } else throw InvalidDataException(data.errors)
        } else throw InvalidDataException(data.errors)
    }

    @GetMapping("/logout")
    fun logout(model: Model): View {
        securityService.logout()
        return RedirectView("/")
    }
}

data class LoginData(val username: String?, val password: String?) : RequestData() {
    var redirectTo: String? = null

    override fun validate() {
        super.validate()
        if (username.isNullOrBlank()) invalidField("username", "Username can't be empty")
        if (password.isNullOrBlank()) invalidField("password", "Password can't be empty")
    }
}

data class RegisterData(
        val username: String?,
        val email: String?,
        val password: String?,
        val gender: String?,
        val passwordConfirm: String?,
        val firstName: String?,
        val lastName: String?,
        var businessName: String?,
        val role: String?) : RequestData() {

    override fun clean() {
        super.clean()
        if (role == "candidate" && !businessName.isNullOrBlank()) businessName = null
    }

    override fun validate() {
        super.validate()
        if (username.isNullOrBlank()) invalidField("username", "Username can't be empty")
        else if (username.length > 30) invalidField("username", "Username cannot be more than 30 characters")
        if (password.isNullOrBlank() || passwordConfirm.isNullOrBlank()) invalidField("password", "Required to input both passwords")
        else if (passwordConfirm != password) invalidField("password", "Passwords do not match")
        if (firstName.isNullOrBlank()) invalidField("firstName", "First name can't be empty")
        if (email.isNullOrBlank()) invalidField("email", "Email can't be empty")
        if (gender.isNullOrBlank()) invalidField("gender", "Gender can't be empty")
        if (role.isNullOrBlank()) invalidField("role", "Role can't be empty")
        else if (role == "employer" && businessName.isNullOrBlank()) invalidField("businessName", "Business name can't be empty")
    }

    fun validUsername(userService: IUserService): Boolean = (userService.findByUsername(username!!) == null)

    fun validEmail(userService: IUserService): Boolean = (userService.findByEmail(email!!) == null)

    fun validBusiness(businessService: IBusinessService) = (businessService.findByName(businessName!!) == null)
}
