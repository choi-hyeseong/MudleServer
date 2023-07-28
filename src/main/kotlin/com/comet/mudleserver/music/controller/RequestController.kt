package com.comet.mudleserver.music.controller

import com.comet.mudleserver.music.dto.MusicResponseDTO
import com.comet.mudleserver.music.model.Message
import com.comet.mudleserver.music.model.Music
import com.comet.mudleserver.music.service.MusicService
import com.comet.mudleserver.music.type.MessageType
import com.comet.mudleserver.response.ObjectResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class RequestController(val musicService: MusicService) {

    @GetMapping("/request")
    fun request(@RequestParam(value = "music") param: String) {
        System.out.println("Music Requested : $param")
        musicService.request(param)
    }

    @GetMapping("/music")
    fun music() : ObjectResponse<MusicResponseDTO> {
        return ObjectResponse("OK", MusicResponseDTO(musicService.music))
    }
}