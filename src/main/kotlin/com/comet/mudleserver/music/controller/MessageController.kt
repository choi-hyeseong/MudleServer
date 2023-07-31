package com.comet.mudleserver.music.controller

import com.comet.mudleserver.music.model.Message
import com.comet.mudleserver.music.service.MusicService
import com.comet.mudleserver.music.type.MessageType
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class MessageController(private val template: SimpMessagingTemplate, val musicService: MusicService) {

    // /pub/message 로 오는 메시지 핸들링
    @MessageMapping("/message")
    fun message(message : Message) {
        System.out.println(message)
        template.convertAndSend("/sub/message", message)
        if (musicService.answerMusic(message.message, message.uuid)) {
            //정답시
            template.convertAndSend("/sub/message", Message.makeSystemMessage(MessageType.ALERT, "${message.sender} 님이 정답을 맞추셨습니다!"))
            template.convertAndSend("/sub/message", Message.makeSystemMessage(MessageType.UPDATE, message.uuid.toString())) //COIN update 요청
        }
    }
}