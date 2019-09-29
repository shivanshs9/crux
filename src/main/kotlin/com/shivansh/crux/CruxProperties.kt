package com.shivansh.crux

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("crux")
class CruxProperties {
    lateinit var appTitle: String
}