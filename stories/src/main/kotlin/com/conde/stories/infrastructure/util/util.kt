package com.conde.stories.infrastructure.util

import org.springframework.jdbc.core.JdbcTemplate
import java.sql.ResultSet
import java.util.*

fun createUUID() = UUID.randomUUID().toString()

inline fun <T> JdbcTemplate.executeQuery(sql: String, crossinline mapper: ResultSet.() -> T): List<T> {
    return query(sql) { response, _ -> response.mapper() }
}
