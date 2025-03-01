package com.conde.stories.controllers

import com.conde.stories.service.HistoryService
import com.conde.stories.service.model.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("api/history/v1")
class HistoryController(private val service: HistoryService) {
    @GetMapping("mock")
    fun getMock() = service.mock

    @GetMapping("user")
    fun getUserStories(@RequestParam userId: String): List<HistoryDto> {
        return service.getUserStories(userId)
    }

    @GetMapping("history")
    fun getHistory(
        @RequestParam userId: String,
        @RequestParam historyId: String,
    ): HistoryDto = service.getHistory(userId, historyId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PutMapping("history")
    suspend fun saveHistory(@RequestParam userId: String, @RequestBody history: HistoryDto) {
        history.elements.forEach {
            if (it.isValid.not()) throw ResponseStatusException(
                HttpStatus.NOT_ACCEPTABLE,
                "elements must have either a image or a text"
            )
        }
        service.saveHistory(userId, history)
    }

    @DeleteMapping("history")
    fun deleteHistory(@RequestParam userId: String, @RequestParam historyId: String): HistoryDto {
        return service.getHistory(userId, historyId)?.also {
            service.deleteHistory(userId, historyId)
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}
