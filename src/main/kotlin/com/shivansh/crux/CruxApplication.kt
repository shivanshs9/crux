package com.shivansh.crux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
@EnableConfigurationProperties(CruxProperties::class)
class CruxApplication: SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<CruxApplication>(*args)
}
