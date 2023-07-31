package com.comet.mudleserver.music.service

import com.comet.mudleserver.music.model.Message
import com.comet.mudleserver.music.model.Music
import com.comet.mudleserver.music.model.User
import com.comet.mudleserver.music.repository.UserRepository
import com.comet.mudleserver.music.type.MessageType
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.jsoup.Jsoup
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class MusicService(private val template: SimpMessagingTemplate, private val userRepository: UserRepository) {

    var music = Music.empty()
    val queue: Queue<Music> = ConcurrentLinkedQueue()
    val mapper = ObjectMapper()
    var isEnded: Boolean = false

    @Async
    fun request(url: String) {
        queue.add(Music(url, 0, getYoutubeTitle(url), false, false, "", UUID.randomUUID()))
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
            println("Music stopped")
            //음악 종료를 위한 variable
            template.convertAndSend("/sub/message", mapper.writeValueAsString(Message.makeSystemMessage(MessageType.REQUEST, "")))
            template.convertAndSend("/sub/message", mapper.writeValueAsString(Message.makeSystemMessage(MessageType.ALERT, "노래가 종료되었습니다.")))
            isEnded = true
            music = Music.empty()
        }
    }

    fun getYoutubeTitle(link: String): String {
        //doc 가져오는데 딜레이 발생가능
        val document = Jsoup.connect("https://www.youtube.com/watch?v=$link").get()
        return document.title().replace(" - YouTube", "").trim()
    }

    fun isMusicAnswered(): Boolean {
        return !(music.isPlaying && !music.isAnswered)
    }

    @Transactional
    fun answerMusic(answer: String, uuid: UUID): Boolean {
        if (!isMusicAnswered() && music.title.contains(answer)) {
            val user = userRepository.findByUuid(uuid)
            if (user.isPresent) {
                user.get().let {
                    it.coin = it.coin?.plus(1)
                    userRepository.save(it)
                    music.isAnswered = true

                }
                return true
            }
        }
        return false
    }
}