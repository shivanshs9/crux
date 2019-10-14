package com.shivansh.crux.controller

import com.shivansh.crux.InvalidDataException
import com.shivansh.crux.InvalidRoleProvidedException
import com.shivansh.crux.model.BusinessMember
import com.shivansh.crux.service.IBusinessService
import com.shivansh.crux.service.ITestService
import com.shivansh.crux.util.BaseResponseData
import com.shivansh.crux.util.HTML5_DATE_FORMATTER
import com.shivansh.crux.util.RequestData
import com.shivansh.crux.util.isNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.text.ParseException
import java.util.*

@RequestMapping("/business/{businessId:[\\d]+}/tests")
@RestController
class TestManageRestController : BaseController() {
    @Autowired
    lateinit var businessService: IBusinessService

    @Autowired
    lateinit var testService: ITestService

    lateinit var businessMember: BusinessMember

    @ModelAttribute
    internal fun verifyEmployee(@PathVariable("businessId") businessId: Long) {
        val loggedInUser = getLoggedInUser()
        businessService.findByUserAndBusinessId(loggedInUser!!, businessId).run {
            if (this == null || this.position != BusinessMember.POSITION.Employee) throw InvalidRoleProvidedException("User is not given business employee")
            businessMember = this@run
        }
    }

    @PostMapping("/")
    fun createNewTest(@ModelAttribute data: TestDetailsData): BaseResponseData {
        data.validate()
        return if (data.isValid()) {
            testService.createNewBusinessTest(businessMember, data)
            data
        } else throw InvalidDataException(data.errors)
    }

    @PatchMapping("/{testId:[\\d]+}")
    fun updateTestDetails(@PathVariable("testId") testId: Long, @ModelAttribute data: TestDetailsData): BaseResponseData {
        data.validate()
        return if (data.isValid()) {
            testService.updateTestWithData(testId, data)
            data
        } else throw InvalidDataException(data.errors)
    }
}

data class TestDetailsData(
        val name: String?, val startTime: String?, val endTime: String?,
        var startDateTime: Date?, var endDateTime: Date?,
        val description: String?, val summary: String?
) : RequestData() {
    override fun clean() {
        super.clean()
        try {
            startDateTime = HTML5_DATE_FORMATTER.parse(startTime)
            endDateTime = HTML5_DATE_FORMATTER.parse(endTime)
        } catch (ignored: ParseException) {
        }
    }

    override fun validate() {
        super.validate()
        if (name.isNullOrBlank()) invalidField("name", "Name cannot be blank")
        when {
            startTime.isNullOrBlank() -> invalidField("startTime", "Start time cannot be blank")
            startDateTime.isNull() -> invalidField("startTime", "Start time should be in yyyy-MM-dd'T'HH:mm format")
            startDateTime!! <= Calendar.getInstance().time -> invalidField("startTime", "Start time should be greater than current time")
        }
        when {
            endTime.isNullOrBlank() -> invalidField("endTime", "End time cannot be blank")
            endDateTime.isNull() -> invalidField("endTime", "End time should be in yyyy-MM-dd'T'HH:mm format")
            endDateTime!! <= Calendar.getInstance().time -> invalidField("endTime", "End time should be greater than current time")
        }
        if (startDateTime != null && endDateTime != null && startDateTime!! >= endDateTime!!) invalidField("endTime", "End time cannot be less than start time")
    }
}