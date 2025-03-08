package com.conde.stories

import com.conde.stories.service.model.*

object Mocks {
    val stories = listOf(
        HistoryDto(
            id = "0",
            title = "Viaje al monte Bromo",
            startDate = System.currentTimeMillis() - 100_000_000,
            elements = listOf(
                HistoryElementDto(
                    id = "1",
                    image = HistoryImageDto("caballo.jpg"),
                ),
                HistoryElementDto(
                    id = "2",
                    text = HistoryTextDto("Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                ),
                HistoryElementDto(
                    id = "3",
                    image = HistoryImageDto("crater_bromo.webp"),
                ),
                HistoryElementDto(
                    id = "4",
                    text = HistoryTextDto("Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java."),
                ),
            ),
        ),
        HistoryDto(
            id = "5",
            title = "Submarinismo en el USS Liberty",
            startDate = System.currentTimeMillis() - 200_000_000,
            elements = listOf(
                HistoryElementDto(
                    id = "6",
                    text = HistoryTextDto("Hice submarinismo dentro del USS Liberty, un barco estadounidense hundido en el noreste de Bali derante la Segunda Gerra Mundial por un submarino japonés."),
                ),
                HistoryElementDto(
                    id = "7",
                    image = HistoryImageDto("buceo_bali.jpg"),
                ),
            ),
        ),
        HistoryDto(
            id = "8",
            title = "Visita a Kuala Lumpur",
            startDate = System.currentTimeMillis() - 300_000_000,
            elements = listOf(
                HistoryElementDto(
                    id = "9",
                    text = HistoryTextDto("Estuve una semana en Kuala Lumpur, la capital de Malasia."),
                ),
                HistoryElementDto(
                    id = "10",
                    image = HistoryImageDto("petronas.jpeg"),
                ),
            ),
        ),
        HistoryDto(
            id = "11",
            title = "Subida al Garbí",
            startDate = System.currentTimeMillis() - 400_000_000,
            endDate = System.currentTimeMillis(),
            elements = listOf(
                HistoryElementDto(
                    id = "12",
                    text = HistoryTextDto("Subí al mirador del Garbí con mi amiga despeinada."),
                ),
                HistoryElementDto(
                    id = "13",
                    image = HistoryImageDto("despeinada.jpeg"),
                ),
            ),
        ),
    )

    val users = listOf(
        UserDto(
            id = "0",
            name = "Nemo",
            description = "Soy literalmente nadie",
            profileImage = "unknown.webp",
        ),
        UserDto(
            id = "00",
            name = "Unnamed",
            description = "I lost my name, I don't remember when.",
            profileImage = null,
        ),
    )
}
