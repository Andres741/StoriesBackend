package com.conde.stories.infrastructure.initializers

import com.conde.stories.service.GreetingService
import com.conde.stories.service.HistoryService
import com.conde.stories.service.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(
    private val db: NamedParameterJdbcTemplate,
    private val greetingService: GreetingService,
    private val userService: UserService,
    private val historyService: HistoryService,
) : CommandLineRunner {

    override fun run(vararg args: String?) = runBlocking {
        //dropTables()
        greetingService.initialize()
        userService.initialize()
        historyService.initialize()

        setMocks()
    }

    private fun dropTables() {
        db.jdbcTemplate.run {
            execute("DROP TABLE IF EXISTS images")
            execute("DROP TABLE IF EXISTS texts")
            execute("DROP TABLE IF EXISTS elements")
            execute("DROP TABLE IF EXISTS stories")
            execute("DROP TABLE IF EXISTS users")
        }
    }

    private suspend fun setMocks() {
        val mockUsers = userService.mock
        val mockStories = historyService.mock
        val firstUser = mockUsers.first()

        mockStories.forEach { mockHistory ->
            historyService.deleteHistory(
                userId = firstUser.id,
                historyId = mockHistory.id,
            )
        }

        mockUsers.forEach { mockUser ->
            userService.deleteUser(mockUser.id)
        }


        mockUsers.forEach { mockUser ->
            userService.createUser(
                userId = mockUser.id,
                userName = mockUser.name,
                description = mockUser.description,
                profileImage = null,
            )
        }

        mockStories.forEach { mockHistory ->
            historyService.saveHistory(
                userId = firstUser.id,
                history = mockHistory,
            )
        }
    }
}
