package com.conde.stories

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/greetings")
class GreetingController {
    @GetMapping
    fun greet() = "Hello stories"

    @GetMapping("dataObject")
    fun dataObject() = Data(22, "Andrés")

    @GetMapping("dataObject2")
    fun dataObject2() = Data2()

    @GetMapping("dataMap")
    fun dataMap() = mapOf(
        "peso" to 80,
        "peso deseado" to 75,
        "siguiente paso" to Data(
            num = 3,
            text = "meses"
        )
    )

    @GetMapping("dataList")
    fun dataList() = listOf("a", "b", "c", 'd', null, 7, 4f, Long.MAX_VALUE)
}

data class Data(
    val num: Int,
    val text: String,
)

class Data2 {
    val num = 9
    val text = "hola"
}
