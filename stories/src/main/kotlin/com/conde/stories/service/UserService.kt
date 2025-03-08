package com.conde.stories.service

import com.conde.stories.infrastructure.util.createUUID
import com.conde.stories.service.model.UserDto
import kotlinx.coroutines.coroutineScope
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.sql.ResultSet

@Service
class UserService(
    private val db: NamedParameterJdbcTemplate,
    private val imageDataService: ImageDataService,
) {
    fun initialize() {
        db.jdbcTemplate.run {
            execute(
                """
                CREATE TABLE IF NOT EXISTS users (
                    id VARCHAR(60) PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    description VARCHAR(1023) NOT NULL,
                    profileImage VARCHAR(1023)
                )
            """.trimIndent()
            )
        }
    }

    suspend fun createUser(
        userName: String,
        description: String,
        profileImage: String?,
        userId: String? = null
    ): UserDto? = coroutineScope {
        if (userName.isBlank()) return@coroutineScope null
        if (!isProfileImageValid(profileImage)) return@coroutineScope null

        val newUserId = userId ?: createUUID()

        db.update(
            "INSERT INTO users (id, name, description, profileImage) VALUES (:id, :name, :description, :profileImage)",
            mapOf("id" to newUserId, "name" to userName, "description" to description, "profileImage" to profileImage),
        )

        return@coroutineScope getUser(newUserId)!!
    }

    suspend fun editUser(user: UserDto): UserDto? = coroutineScope {
        if (!existsUser(userId = user.id)) return@coroutineScope null
        if (!isProfileImageValid(user.profileImage)) return@coroutineScope null

        db.update(
            "UPDATE users SET name = :name, description = :description, profileImage = :profileImage WHERE id = :id",
            mapOf(
                "id" to user.id,
                "name" to user.name,
                "description" to user.description,
                "profileImage" to user.profileImage
            ),
        )
        return@coroutineScope getUser(user.id)
    }

    private fun isProfileImageValid(profileImage: String?): Boolean {
        return profileImage == null || imageDataService.existsImage(profileImage)
    }

    fun existsUser(userId: String) = getUser(userId = userId) != null

    fun getUser(userId: String): UserDto? {
        return db.query("SELECT * FROM users WHERE id = :id", mapOf("id" to userId)) { it, _ ->
            it.toUser()
        }.firstOrNull()
    }

    fun getAllUsers(): List<UserDto> {
        return db.query("SELECT * FROM users") { it, _ -> it.toUser() }
    }

    fun deleteUser(userId: String): UserDto? {
        return getUser(userId)?.also {
            db.update("DELETE FROM users WHERE id = :id", mapOf("id" to userId))
        }
    }

    fun ResultSet.toUser() = UserDto(
        id = getString("id"),
        name = getString("name"),
        description = getString("description"),
        profileImage = getString("profileImage"),
    )
}

