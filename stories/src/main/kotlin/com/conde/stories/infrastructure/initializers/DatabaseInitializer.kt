package com.conde.stories.infrastructure.initializers

import org.springframework.boot.CommandLineRunner
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(private val jdbcTemplate: JdbcTemplate) : CommandLineRunner {
    override fun run(vararg args: String?) {
        initializeGreetings()
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
}
