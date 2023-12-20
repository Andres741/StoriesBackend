package com.conde.stories.infrastructure.initializers

import org.springframework.boot.CommandLineRunner
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(private val jdbcTemplate: JdbcTemplate) : CommandLineRunner {
    override fun run(vararg args: String?) {
        initializeGreetings()
        initializeUsers()
        initializeHistory()
    }

    private fun initializeGreetings() {
        jdbcTemplate.execute("""
			CREATE TABLE IF NOT EXISTS greetings (
				id VARCHAR(60) PRIMARY KEY,
				text VARCHAR(255) NOT NULL,
				number INTEGER NOT NULL
			)
		""".trimIndent())
        jdbcTemplate.execute("DELETE FROM greetings WHERE id = '0'")
        jdbcTemplate.execute("INSERT INTO greetings (id, text, number) VALUES ('0', 'Hello world', 987)")
    }

    private fun initializeUsers() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS users (
				id VARCHAR(60) PRIMARY KEY,
				name VARCHAR(255) NOT NULL
			)
        """.trimIndent())
    }

    private fun initializeHistory() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS stories (
				id VARCHAR(60) PRIMARY KEY,
				title VARCHAR(255) NOT NULL,
                startDate BIGINT NOT NULL,
                endDate BIGINT,
                version INT
			)
        """.trimIndent())

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS element (
				id VARCHAR(60) PRIMARY KEY,
                historyId VARCHAR(60),
                FOREIGN KEY (historyId) REFERENCES stories(id)
			)
        """.trimIndent())

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS text (
				id VARCHAR(60) PRIMARY KEY,
				text VARCHAR(4095) NOT NULL,
                elementId VARCHAR(60),
                FOREIGN KEY (elementId) REFERENCES element(id)
			)
        """.trimIndent())

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS image (
				id VARCHAR(60) PRIMARY KEY,
				imageUrl VARCHAR(1023) NOT NULL,
                elementId VARCHAR(60),
                FOREIGN KEY (elementId) REFERENCES element(id)
			)
        """.trimIndent())
    }
}
