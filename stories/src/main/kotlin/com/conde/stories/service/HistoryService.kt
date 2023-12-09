package com.conde.stories.service

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class HistoryService(val db: JdbcTemplate) {

}
