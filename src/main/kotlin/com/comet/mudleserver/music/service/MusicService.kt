package com.comet.mudleserver.music.service

import com.comet.mudleserver.music.model.Message
import com.comet.mudleserver.music.model.Music
import com.comet.mudleserver.music.model.User
import com.comet.mudleserver.music.repository.UserRepository
import com.comet.mudleserver.music.type.MessageType
import com.comet.mudleserver.response.Response
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.jsoup.Jsoup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class MusicService(private val template: SimpMessagingTemplate, private val userService: UserService) {

    var music = Music.empty()
    val queue: Queue<Music> = ConcurrentLinkedQueue()
    val mapper = ObjectMapper()
    var isEnded: Boolean = false

    @Async
    //성공여부 Boolean형으로 return
    fun request(uuid: UUID, url: String) : CompletableFuture<ResponseEntity<Response>> {
        val title = getYoutubeTitle(url)
        val result : ResponseEntity<Response>
        //youtube link
        if (title.equals("- YouTube", true) || title.contains("YouTube"))
            //올바르지 않은 경우 replace가 안됨
            result = ResponseEntity(Response("올바른 링크가 아닙니다."), HttpStatus.BAD_REQUEST)
        else if (!userService.isUserExists(uuid))
            result = ResponseEntity(Response("유저가 존재하지 않습니다."), HttpStatus.BAD_REQUEST)
        //isExists 거침, 1씩 소모
        else if (userService.getUser(uuid)!!.coin!! < 1)
            result = ResponseEntity(Response("코인이 모자랍니다."), HttpStatus.BAD_REQUEST)
        else {
            result = ResponseEntity(Response("음악 신청이 완료되었습니다! - $title"), HttpStatus.OK)
            userService.consumeCoin(uuid, 1)
            queue.add(Music(url, 0, title, false, false, "", UUID.randomUUID()))
        }
        println(result.body.toString())
        return CompletableFuture.completedFuture(result)
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
            if (userService.isUserExists(uuid)) {
                userService.giveCoin(uuid, 1)
                music.isAnswered = true
                return true
            }
        }
        return false
    }
}