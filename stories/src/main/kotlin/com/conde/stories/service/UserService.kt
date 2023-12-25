package com.conde.stories.service

import com.conde.stories.infrastructure.util.createUUID
import com.conde.stories.service.model.UserDto
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.sql.ResultSet

@Service
class UserService(private val db: NamedParameterJdbcTemplate) {
    val mock = listOf(UserDto(id = "0", name = "Nemo", description = "I am literally nobody", profileImage = ""), UserDto(id = "00", name = "Innominado", description = "Perdí mi nombre, no recuerdo cuando.", profileImage = null))

    fun initialize() {
        db.jdbcTemplate.run {
            execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id VARCHAR(60) PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    description VARCHAR(1023) NOT NULL,
                    profileImage VARCHAR(1023)
                )
            """.trimIndent())
        }
    }

    fun createUser(userName: String, description: String, profileImage: String?, userId: String? = null): UserDto? {
        val newUserId = userId ?: createUUID()
        db.update(
            "INSERT INTO users (id, name, description, profileImage) VALUES (:id, :name, :description, :profileImage)",
            mapOf("id" to newUserId, "name" to userName, "description" to description, "profileImage" to profileImage),
        )
        return getUser(newUserId)
    }

    fun getUser(userId: String): UserDto? {
        return db.query("SELECT * FROM users WHERE id = :id", mapOf("id" to userId)) { it, _ ->
            it.toUser()
        }.firstOrNull()
    }

    fun getAllUsers(): List<UserDto> {
        return db.query("SELECT * FROM users") { it, _ -> it.toUser() }
    }

    fun deleteUser(userId: String): UserDto? {
        return getUser(userId).also {
            db.update("DELETE FROM users WHERE id = :id", mapOf("id" to userId))
        }
    }
}

fun ResultSet.toUser() = UserDto(
    id = getString("id"),
    name = getString("name"),
    description = getString("description"),
    profileImage = getString("profileImage")
)
