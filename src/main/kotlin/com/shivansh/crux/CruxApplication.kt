package com.shivansh.crux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(CruxProperties::class)
class CruxApplication

fun main(args: Array<String>) {
	runApplication<CruxApplication>(*args)
}
