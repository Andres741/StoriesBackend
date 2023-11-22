package com.conde.stories.infrastructure.loggers

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver

@Component
class AppExceptionResolver : AbstractHandlerExceptionResolver() {

    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)

    override fun doResolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception
    ): ModelAndView? {
        logger.error("Application error in: [${ex.javaClass.name}]", ex)
        return null
    }
}
