package com.kafkakotlinsample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaKotlinSampleApplication

fun main(args: Array<String>) {
	println("Hello Kotlin Kafka!")
	runApplication<KafkaKotlinSampleApplication>(*args)
}
