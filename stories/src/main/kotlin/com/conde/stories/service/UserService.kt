package com.conde.stories.service

import com.conde.stories.infrastructure.util.createUUID
import com.conde.stories.service.model.UserDto
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.sql.ResultSet

@Service
class UserService(
    private val db: NamedParameterJdbcTemplate,
    private val imageDataService: ImageDataService,
) {
    val mock = listOf(UserDto(id = "0", name = "Nemo", description = "Soy literalmente nadie", profileImage = "https://cdn1.iconfinder.com/data/icons/user-pictures/100/unknown-512.png"), UserDto(id = "00", name = "Unnamed", description = "I lost my name, I don't remember when.", profileImage = null))

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

    suspend fun createUser(userName: String, description: String, profileImageData: ByteArray?, userId: String? = null): UserDto? = coroutineScope{
        if (userName.isBlank()) return@coroutineScope null

        val newUserId = userId ?: createUUID()
        val profileImageId = profileImageData?.let { createUUID() }

        profileImageId?.let {
            launch {
                imageDataService.saveAsJpegImage(profileImageId, profileImageData)
            }
        }

        db.update(
            "INSERT INTO users (id, name, description, profileImage) VALUES (:id, :name, :description, :profileImage)",
            mapOf("id" to newUserId, "name" to userName, "description" to description, "profileImage" to profileImageId),
        )

        return@coroutineScope getUser(newUserId)!!
    }

    suspend fun editUser(user: UserDto, profileImageData: ByteArray?): UserDto? = coroutineScope {
        if (!existsUser(userId = user.id)) return@coroutineScope null

        val profileImageId = profileImageData?.let { createUUID() }
        profileImageId?.let {
            launch {
                imageDataService.saveAsJpegImage(profileImageId, profileImageData)
            }
        }
        db.update(
            "REPLACE INTO users (id, name, description, profileImage) VALUES (:id, :name, :description, :profileImage)",
            mapOf("id" to user.id, "name" to user.name, "description" to user.description, "profileImage" to profileImageId),
        )
        return@coroutineScope getUser(user.id)
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
}

fun ResultSet.toUser() = UserDto(
    id = getString("id"),
    name = getString("name"),
    description = getString("description"),
    profileImage = getString("profileImage")
)
