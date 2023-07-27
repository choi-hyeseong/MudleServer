package com.comet.mudleserver.music.service

import com.comet.mudleserver.music.model.Message
import com.comet.mudleserver.music.model.Music
import com.comet.mudleserver.music.type.MessageType
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class MusicService(private val template: SimpMessagingTemplate) {

    val queue: Queue<Music> = ConcurrentLinkedQueue()
    val mapper = ObjectMapper()
    var isEnded: Boolean = false
    fun request(music: Music) {
        queue.add(music)
    }

    @Scheduled(fixedRateString = "\${music.interval}")
    fun schedule() {
        System.out.println("Scheduling Queue..")
        if (queue.isNotEmpty()) {
            val music = queue.poll()
            template.convertAndSend("/sub/message", mapper.writeValueAsString(Message.makeSystemMessage(MessageType.REQUEST, music.link)))
            template.convertAndSend("/sub/message", mapper.writeValueAsString(Message.makeSystemMessage(MessageType.ALERT, "다음 노래가 재생됩니다!")))
            isEnded = false
        }
        else if (!isEnded) {
            //음악 종료를 위한 variable
            template.convertAndSend("/sub/message", mapper.writeValueAsString(Message.makeSystemMessage(MessageType.REQUEST, "")))
            template.convertAndSend("/sub/message", mapper.writeValueAsString(Message.makeSystemMessage(MessageType.ALERT, "노래가 종료되었습니다.")))
            isEnded = true
        }
    }

}