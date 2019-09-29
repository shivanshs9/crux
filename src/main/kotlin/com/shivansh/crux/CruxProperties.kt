package com.shivansh.crux

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("crux")
class CruxProperties {
    lateinit var title: String
}