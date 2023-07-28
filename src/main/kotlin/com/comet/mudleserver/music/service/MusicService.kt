package com.comet.mudleserver.music.service

import com.comet.mudleserver.music.model.Message
import com.comet.mudleserver.music.model.Music
import com.comet.mudleserver.music.type.MessageType
import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class MusicService(private val template: SimpMessagingTemplate) {

    var music = Music.empty()
    val queue: Queue<Music> = ConcurrentLinkedQueue()
    val mapper = ObjectMapper()
    var isEnded: Boolean = false

    @Async
    fun request(url: String) {
        queue.add(Music(url, 0, getYoutubeTitle(url), false))
    }

    @Scheduled(fixedRateString = "\${music.interval}")
    fun schedule() {

        if (queue.isNotEmpty()) {
            println("Scheduling Queue..")
            music = queue.poll().apply {
                isPlaying = true
                startTime = System.currentTimeMillis()
            }
            println("$music")
            template.convertAndSend("/sub/message", mapper.writeValueAsString(Message.makeSystemMessage(MessageType.REQUEST, music.link)))
            template.convertAndSend("/sub/message", mapper.writeValueAsString(Message.makeSystemMessage(MessageType.ALERT, "다음 노래가 재생됩니다!")))
            isEnded = false
        }
        else if (!isEnded) {
            //음악 종료를 위한 variable
            template.convertAndSend("/sub/message", mapper.writeValueAsString(Message.makeSystemMessage(MessageType.REQUEST, "")))
            template.convertAndSend("/sub/message", mapper.writeValueAsString(Message.makeSystemMessage(MessageType.ALERT, "노래가 종료되었습니다.")))
            isEnded = true
            music = Music.empty()
        }
    }

    fun getYoutubeTitle(link : String) : String {
        //doc 가져오는데 딜레이 발생가능
        val document = Jsoup.connect("https://www.youtube.com/watch?v=$link").get()
        return document.title().replace(" - YouTube", "").trim()
    }

}