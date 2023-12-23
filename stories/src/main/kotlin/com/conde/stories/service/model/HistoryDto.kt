package com.conde.stories.service.model

data class HistoryDto(
    val id: String,
    val title: String,
    val startDate: Long,
    val endDate: Long? = null,
    val version: Int = 0,
    val elements: List<HistoryElementDto>,
)

data class HistoryElementDto(
    val id: String,
    val text: HistoryTextDto? = null,
    val image: HistoryImageDto? = null,
)

inline val HistoryElementDto.isValid get() = listOfNotNull(text, image).size == 1

data class HistoryTextDto(
    val text: String,
)

data class HistoryImageDto(
    val imageUrl: String,
)

