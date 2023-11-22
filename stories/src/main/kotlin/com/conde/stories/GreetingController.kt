package com.conde.stories

import com.conde.stories.data.Data
import com.conde.stories.data.Data2
import com.conde.stories.infrastructure.util.AnyMap
import com.conde.stories.service.GreetingService
import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/greetings")
class GreetingController(val service: GreetingService) {
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
    fun dataList() = listOf("a", "b", "c", 'd', null, 7, 4f, Long.MAX_VALUE, Data2())

    @GetMapping("get500")
    fun get500(): Nothing = throw Exception()

    @GetMapping("dataSuspend")
    suspend fun dataSuspend(): AnyMap {
        val delay = 1000L
        delay(delay)
        return mapOf(
            "kind" to "suspended data",
            "delay" to mapOf(
                "unit" to "ms",
                "amount" to delay
            )
        )
    }

    @GetMapping("dataFromService")
    suspend fun dataFromService() = service.simpleServiceData()

    @GetMapping("someData")
    fun getSomeData() = service.getSomeData()

    @PostMapping("someData")
    fun putSomeData(@RequestBody data: Data) {
        service.saveSomeData(data)
    }

    @DeleteMapping("someData")
    fun deleteAllData() {
        service.deleteAllData()
    }
}
