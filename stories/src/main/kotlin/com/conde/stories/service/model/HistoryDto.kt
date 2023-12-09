package com.conde.stories.service.model

data class HistoryDto(
    val id: String,
    val title: String,
    val startDate: Long,
    val endDate: Long? = null,
    val elements: List<HistoryElementDto>,
)

data class HistoryElementDto(
    val id: String,
    val text: HistoryTextDto? = null,
    val image: HistoryImageDto? = null,
)

data class HistoryTextDto(
    val text: String,
)

data class HistoryImageDto(
    val imageUrl: String,
)
