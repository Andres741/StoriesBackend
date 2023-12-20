package com.conde.stories.service

import com.conde.stories.service.model.Data
import com.conde.stories.infrastructure.util.AnyMap
import com.conde.stories.infrastructure.util.createUUID
import com.conde.stories.infrastructure.util.executeQuery
import kotlinx.coroutines.delay
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.util.*

@Service
class GreetingService(val db: JdbcTemplate) {
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
