package com.shivansh.crux.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "test_tag")
class TestTag {
    @EmbeddedId
    lateinit var id: TestTagId

    @MapsId(value = "testId")
    @ManyToOne
    @JoinColumn(name = "testId")
    lateinit var test: Test
}

@Embeddable
class TestTagId : Serializable {
    lateinit var tag: String
    var testId: Long = 0
}
