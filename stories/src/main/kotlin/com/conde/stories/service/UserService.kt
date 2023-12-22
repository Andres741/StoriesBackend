package com.conde.stories.service

import com.conde.stories.infrastructure.util.createUUID
import com.conde.stories.infrastructure.util.executeQuery
import com.conde.stories.service.model.UserDto
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.sql.ResultSet

@Service
class UserService(private val db: NamedParameterJdbcTemplate) {
    fun createUser(userName: String): UserDto? {
        val newUserId = createUUID()
        db.update("INSERT INTO users (id, name) VALUES (:id, :name)", mapOf("id" to newUserId, "name" to userName))
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
)
