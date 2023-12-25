package com.conde.stories.service

import com.conde.stories.service.model.Data
import com.conde.stories.infrastructure.util.AnyMap
import com.conde.stories.infrastructure.util.createUUID
import com.conde.stories.infrastructure.util.executeQuery
import kotlinx.coroutines.delay
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.sql.ResultSet

@Service
class GreetingService(private val db: JdbcTemplate) {

    fun initialize() {
        db.run {
            execute("""
                CREATE TABLE IF NOT EXISTS greetings (
                    id VARCHAR(60) PRIMARY KEY,
                    text VARCHAR(255) NOT NULL,
                    number INTEGER NOT NULL
                )
            """.trimIndent())
            execute("DELETE FROM greetings WHERE id = '0'")
            execute("INSERT INTO greetings (id, text, number) VALUES ('0', 'Hello world', 987)")
        }
    }

    suspend fun simpleServiceData(): AnyMap {
        delay(2000)
        return mapOf(
            "kind" to "data from a service"
        )
    }

    fun getSomeData(): List<Data> {
        return db.executeQuery("SELECT * FROM greetings") { getData() }
    }

    fun saveSomeData(data: Data) {
        val id = createUUID()
        db.update("INSERT INTO greetings (id, text, number) VALUES ( ?, ?, ? )", id, data.text, data.num)
    }

    fun deleteAllData() {
        db.update("DELETE FROM greetings")
    }
}

fun ResultSet.getData() = Data(
    num = getInt("number"),
    text = getString("text") ?: ""
)
