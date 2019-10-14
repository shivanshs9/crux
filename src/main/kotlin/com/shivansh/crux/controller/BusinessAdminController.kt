package com.shivansh.crux.controller

import com.shivansh.crux.InvalidDataException
import com.shivansh.crux.InvalidRoleProvidedException
import com.shivansh.crux.model.BusinessMember
import com.shivansh.crux.service.IBusinessService
import com.shivansh.crux.util.RequestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/business/admin")
class BusinessAdminController : BaseController() {
    @Autowired
    lateinit var businessService: IBusinessService

    internal fun getBusiness() = getLoggedInUser()?.let {
        businessService.findByUser(it)
    }!!.business

    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        model["title"] = "Dashboard"
        model["business"] = getBusiness()
        return "business-admin-dashboard"
    }

    @GetMapping("/members")
    fun membersListing(model: Model): String {
        model["title"] = "Business members"
        val business = getBusiness().also { model["business"] = it }
        model["members"] = businessService.findBusinessMembers(business)
        return "business-admin-members"
    }
}

@RestController
@RequestMapping("/business/{id:[\\d]+}")
class BusinessAdminRestController : BaseController() {
    @Autowired
    lateinit var businessService: IBusinessService

    @ModelAttribute
    internal fun verifyAdmin(@PathVariable("id") businessId: Long) {
        val loggedInUser = userService.findByUsername(securityService.findLoggedInUsername()!!)
        businessService.findByUserAndBusinessId(loggedInUser!!, businessId).run {
            if (this == null || this.position != BusinessMember.POSITION.Owner) throw InvalidRoleProvidedException("User is not given business owner")
        }
    }

    internal fun getBusiness() = userService.findByUsername(securityService.findLoggedInUsername()!!)?.let {
        businessService.findByUser(it)
    }!!.business

    @PostMapping("/details")
    fun editDetails(@PathVariable id: Long, @ModelAttribute data: BusinessDetailsData): BusinessDetailsData {
        data.validate()
        return if (data.isValid()) {
            businessService.updateBusinessDetails(id, data)
            data
        } else throw InvalidDataException(data.errors)
    }

    @PostMapping("/members")
    fun addMember(@PathVariable id: Long, @ModelAttribute data: BusinessMembersData): BusinessMembersData {
        data.validate()
        if (data.isValid()) {
            userService.findByUsername(data.username!!)?.let {
                if (businessService.addNewMember(getBusiness(), it, data.positionEnum) == null) {
                    data.invalidField("username", "User already employee of a business")
                }
            } ?: data.invalidField("username", "Username not found")
            return if (data.isValid()) data
            else throw InvalidDataException(data.errors)
        } else throw InvalidDataException(data.errors)
    }

    @DeleteMapping("/members/{memberId:[\\d]+}")
    fun deleteMember(@PathVariable memberId: Long, @PathVariable id: Long) {
        businessService.removeMember(memberId)
    }
}

data class BusinessDetailsData(
        val name: String?, var country: String = "IN", val state: String?,
        val city: String?, val address: String?, val logo: String?,
        val category: String?
) : RequestData() {
    override fun clean() {
        super.clean()
        if (country.isBlank()) country = "IN"
    }

    override fun validate() {
        super.validate()
        if (name.isNullOrBlank()) invalidField("name", "Name cannot be empty")
        if (category.isNullOrBlank()) invalidField("category", "Category cannot be empty")
    }
}

data class BusinessMembersData(val username: String?, var position: String?, var positionEnum: BusinessMember.POSITION = BusinessMember.POSITION.Employee) : RequestData() {
    override fun clean() {
        super.clean()
        positionEnum = when {
            position.equals("owner", ignoreCase = true) -> BusinessMember.POSITION.Owner
            else -> BusinessMember.POSITION.Employee
        }
    }

    override fun validate() {
        super.validate()
        if (username.isNullOrBlank()) invalidField("username", "Username cannot be empty")
    }
}
