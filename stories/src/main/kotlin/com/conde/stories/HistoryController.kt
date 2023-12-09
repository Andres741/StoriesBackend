package com.conde.stories

import com.conde.stories.service.HistoryService
import com.conde.stories.service.model.HistoryDto
import com.conde.stories.service.model.HistoryElementDto
import com.conde.stories.service.model.HistoryImageDto
import com.conde.stories.service.model.HistoryTextDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/history")
class HistoryController(val service: HistoryService) {
    @GetMapping("mock")
    fun getMock() = listOf(
        HistoryDto(
            id = "0",
            title = "Viaje al monte Bromo",
            startDate = System.currentTimeMillis() - 1_000_000,
            endDate = System.currentTimeMillis(),
            elements = listOf(
                HistoryElementDto(id = "1", image = HistoryImageDto("https://harindabama.files.wordpress.com/2012/10/bromo11.jpg")),
                HistoryElementDto(id = "2", text = HistoryTextDto("Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java.")),
                HistoryElementDto(id = "3", image = HistoryImageDto("https://www.elperiodicodelturismo.com/images/crater-monte-bromo.webp")),
                HistoryElementDto(id = "4", text = HistoryTextDto("Monté a caballo en el mar de arena que rodea al volcán activo Bromo, en el este de Java.")),
            ),
        ),
        HistoryDto(
            id = "5",
            title = "Submarinismo en el USS Liberty",
            startDate = System.currentTimeMillis() - 2_000_000,
            elements = listOf(
                HistoryElementDto(id = "6", text = HistoryTextDto("Hice submarinismo dentro del USS Liberty, un barco estadounidense hundido en el noreste de Bali derante la Segunda Gerra Mundial por un submarino japonés.")),
                HistoryElementDto(id = "7", image = HistoryImageDto( "https://media.tacdn.com/media/attractions-splice-spp-674x446/07/95/10/33.jpg")),
            ),
        ),
        HistoryDto(
            id = "8",
            title = "Visita a Kuala Lumpur",
            startDate = System.currentTimeMillis() - 3_000_000,
            elements = listOf(
                HistoryElementDto(id = "9", text = HistoryTextDto("Estuve una semana en Kuala Lumpur, la capital de Malasia.")),
                HistoryElementDto(id = "10", image = HistoryImageDto("https://images.pexels.com/photos/433989/pexels-photo-433989.jpeg")),
            ),
        ),
    )
}
