package com.shivansh.crux.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "mcq_submission")
class McqSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var submittedTime: Date
    var score: Int = 0

    @ManyToOne
    @JoinColumn(name = "participantId")
    lateinit var participant: TestParticipant

    @ManyToOne
    @JoinColumn(name = "questionId")
    lateinit var question: McqQuestion

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "submission")
    lateinit var options: Set<McqSubmissionOption>
}