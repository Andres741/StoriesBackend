package com.conde.stories.infrastructure.util

import com.conde.stories.controllers.ImageController.Companion.IMAGES_API_PATH
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.InetAddress

@Component
class BaseUrlResolver {

    private val serverAddress: String = InetAddress.getLocalHost().hostAddress
    @Value("\${server.port}")
    private lateinit var serverPort: String

    val baseURL by lazy { "http://$serverAddress:$serverPort" }

    fun imageIdToURL(imageName: String) = "${baseURL}/$IMAGES_API_PATH/$imageName"
}
