package com.conde.stories.infrastructure.util

import org.springframework.jdbc.core.JdbcTemplate
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

fun createUUID() = UUID.randomUUID().toString()

inline fun <T> JdbcTemplate.executeQuery(sql: String, crossinline mapper: ResultSet.() -> T): List<T> {
    return query(sql) { response, _ -> response.mapper() }
}

fun ResultSet.getStringOrNull(columnLabel: String): String? {
    return try {
        getString(columnLabel)
    } catch (e: SQLException) {
        null
    }
}
