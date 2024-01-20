package com.conde.stories.service

import com.conde.stories.infrastructure.util.getStringOrNull
import com.conde.stories.service.model.HistoryDto
import com.conde.stories.service.model.HistoryElementDto
import com.conde.stories.service.model.HistoryImageDto
import com.conde.stories.service.model.HistoryTextDto
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.sql.ResultSet

@Service
class HistoryService(private val db: NamedParameterJdbcTemplate) {
    val mock = listOf(HistoryDto(id = "0", title = "Viaje al monte Bromo", startDate = System.currentTimeMillis() - 1_000_000, endDate = System.currentTimeMillis(), elements = listOf(HistoryElementDto(id = "1", image = HistoryImageDto("https://harindabama.files.wordpress.com/2012/10/bromo11.jpg")), HistoryElementDto(id = "2", text = HistoryTextDto("Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java.")), HistoryElementDto(id = "3", image = HistoryImageDto("https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp")), HistoryElementDto(id = "4", text = HistoryTextDto("Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java.")))), HistoryDto(id = "5", title = "Submarinismo en el USS Liberty", startDate = System.currentTimeMillis() - 2_000_000, elements = listOf(HistoryElementDto(id = "6", text = HistoryTextDto("Hice submarinismo dentro del USS Liberty, un barco estadounidense hundido en el noreste de Bali derante la Segunda Gerra Mundial por un submarino japonés.")), HistoryElementDto(id = "7", image = HistoryImageDto( "https://media.tacdn.com/media/attractions-splice-spp-674x446/07/95/10/33.jpg")))), HistoryDto(id = "8", title = "Visita a Kuala Lumpur", startDate = System.currentTimeMillis() - 3_000_000, elements = listOf(HistoryElementDto(id = "9", text = HistoryTextDto("Estuve una semana en Kuala Lumpur, la capital de Malasia.")), HistoryElementDto(id = "10", image = HistoryImageDto("https://images.pexels.com/photos/433989/pexels-photo-433989.jpeg")))))

    fun initialize() {
        db.jdbcTemplate.run {
            execute("""
                CREATE TABLE IF NOT EXISTS stories (
                    id VARCHAR(60),
                    title VARCHAR(255) NOT NULL,
                    startDate BIGINT NOT NULL,
                    endDate BIGINT,
                    version INT,
                    userId VARCHAR(60),
                    PRIMARY KEY (id, userId),
                    FOREIGN KEY (userId) REFERENCES users(id)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE
                )
            """.trimIndent())

            execute("""
                CREATE TABLE IF NOT EXISTS texts (
                    id VARCHAR(60) PRIMARY KEY,
                    text VARCHAR(4095) NOT NULL,
                    position INT NOT NULL,
                    historyId VARCHAR(60),
                    FOREIGN KEY (historyId) REFERENCES stories(id)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE
                )
            """.trimIndent())

            execute("""
                CREATE TABLE IF NOT EXISTS images (
                    id VARCHAR(60) PRIMARY KEY,
                    imageUrl VARCHAR(1023) NOT NULL,
                    position INT NOT NULL,
                    historyId VARCHAR(60),
                    FOREIGN KEY (historyId) REFERENCES stories(id)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE
                )
            """.trimIndent())
        }
    }

    fun getHistory(userId: String, historyId: String): HistoryDto? {
        val elements = getElementsByHistory(historyId)
        return db.query("SELECT * FROM stories WHERE userId = :userId AND id = :id", mapOf("userId" to userId, "id" to historyId)) { it, _ ->
            it.toHistory(elements)
        }.firstOrNull()
    }

    fun getUserStories(userId: String): List<HistoryDto> {
        val stories = db.query(
            "SELECT * FROM stories WHERE userId = :userId",
            mapOf("userId" to userId)
        ) { it, _ -> it.toHistory() }

        return stories.map { history ->
            val elements = getElementsByHistory(history.id)
            history.copy(elements = elements)
        }
    }

    fun getElementsByHistory(historyId: String): List<HistoryElementDto> {
        val elements = getTextsByHistory(historyId) + getImagesByHistory(historyId)
        return elements.asSequence().sortedBy { it.first }.map { it.second }.toList()
    }

    fun getTextsByHistory(historyId: String): List<Pair<Int, HistoryElementDto>> {
        return db.query("SELECT * FROM texts WHERE historyId = :historyId", mapOf("historyId" to historyId)) {it, _ ->
            it.toHistoryElement()
        }
    }

    fun getImagesByHistory(historyId: String): List<Pair<Int, HistoryElementDto>> {
        return db.query("SELECT * FROM images WHERE historyId = :historyId", mapOf("historyId" to historyId)) {it, _ ->
            it.toHistoryElement()
        }
    }

    suspend fun saveHistory(userId: String, history: HistoryDto) {
        deleteElements(historyId = history.id)

        db.update(
            """
                REPLACE INTO stories (id, title, startDate, endDate, version, userId) 
                VALUES (:id, :title, :startDate, :endDate, :version, :userId)
            """.trimIndent(),
            mapOf(
                "id" to history.id, "title" to history.title, "startDate" to history.startDate,
                "endDate" to history.endDate, "version" to history.version, "userId" to userId
            )
        )

        coroutineScope {
            history.elements.forEachIndexed { index, element ->
                launch {
                    saveElement(historyId = history.id, element = element, position = index)
                }
            }
        }
    }

    fun saveElement(historyId: String, element: HistoryElementDto, position: Int) {
        element.text?.let { text ->
            saveTextElement(elementId = element.id, text = text, position = position, historyId = historyId)
        }
        element.image?.let { image ->
            saveImageElement(elementId = element.id, image = image, position = position, historyId = historyId)
        }
    }

    fun saveTextElement(elementId: String, text: HistoryTextDto, position: Int, historyId: String) {
        db.update(
            "REPLACE INTO texts (id, text, position, historyId) VALUES (:id, :text, :position, :historyId)",
            mapOf("id" to elementId, "text" to text.text, "position" to position, "historyId" to historyId)
        )
    }

    fun saveImageElement(elementId: String, image: HistoryImageDto, position: Int, historyId: String) {
        db.update(
            "REPLACE INTO images (id, imageUrl, position, historyId) VALUES (:id, :imageUrl, :position, :historyId)",
            mapOf("id" to elementId, "imageUrl" to image.imageUrl, "position" to position, "historyId" to historyId)
        )
    }


    fun deleteHistory(userId: String, historyId: String) {
        deleteElements(historyId = historyId)
        db.update("DELETE FROM stories WHERE userId = :userId AND id = :id", mapOf("userId" to userId, "id" to historyId))
    }

    fun deleteElements(historyId: String) {
        deleteTexts(historyId)
        deleteImages(historyId)
    }

    fun deleteTexts(historyId: String) {
        db.update("DELETE FROM texts WHERE historyId = :historyId", mapOf("historyId" to historyId))
    }

    fun deleteImages(historyId: String) {
        db.update("DELETE FROM images WHERE historyId = :historyId", mapOf("historyId" to historyId))
    }


    private fun ResultSet.toHistory(elements: List<HistoryElementDto> = emptyList()) = HistoryDto(
        id = getString("id"),
        title = getString("title"),
        startDate = getLong("startDate"),
        endDate = getObject("endDate") as? Long,
        version = getInt("version"),
        elements = elements
    )

    private fun ResultSet.toHistoryElement(): Pair<Int, HistoryElementDto>? {
        val id = getString("id")
        val position = getInt("position")

        getStringOrNull("text")?.let { text ->
            return position to HistoryElementDto(
                id = id,
                text = HistoryTextDto(text = text),
            )
        }

        getStringOrNull("imageUrl")?.let { image ->
            return position to HistoryElementDto(
                id = id,
                image = HistoryImageDto(imageUrl = image),
            )
        }

        return null
    }
}
