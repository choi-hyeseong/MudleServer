package com.comet.mudleserver.music.controller

import com.comet.mudleserver.music.model.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class MessageController(private val template: SimpMessagingTemplate) {

    // /pub/message 로 오는 메시지 핸들링
    @MessageMapping("/message")
    fun message(message : Message) {
        System.out.println(message)
        template.convertAndSend("/sub/message", message)
    }
}