package com.conde.stories

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StoriesApplication

fun main(args: Array<String>) {
	runApplication<StoriesApplication>(*args)
}
