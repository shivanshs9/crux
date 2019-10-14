package com.shivansh.crux.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "mcq_submission_option")
class McqSubmissionOption {
    @EmbeddedId
    lateinit var id: McqSubmissionOptionId

    @MapsId(value = "submissionId")
    @ManyToOne
    @JoinColumn(name = "submissionId")
    lateinit var submission: McqSubmission

    @MapsId(value = "optionId")
    @ManyToOne
    @JoinColumn(name = "optionId")
    lateinit var option: McqOption
}

@Embeddable
class McqSubmissionOptionId : Serializable {
    var submissionId: Long = 0
    var optionId: Long = 0
}
