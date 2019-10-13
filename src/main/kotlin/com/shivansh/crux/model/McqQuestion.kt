package com.shivansh.crux.model

import com.shivansh.crux.util.formatCompleteDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "mcq_question")
class McqQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var title: String
    lateinit var description: String
    var marks: Int = 0
    lateinit var createdTime: Date

    @ManyToOne
    @JoinColumn(name = "testId")
    lateinit var test: Test

    @ManyToOne
    @JoinColumn(name = "problemSetterId")
    lateinit var problemSetter: ProblemSetter

    val createdTimeString: String
        get() = formatCompleteDate(createdTime)
}