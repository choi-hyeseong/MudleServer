package com.comet.mudleserver.music.controller

import com.comet.mudleserver.music.dto.MusicRequestDTO
import com.comet.mudleserver.music.dto.MusicResponseDTO
import com.comet.mudleserver.music.service.MusicService
import com.comet.mudleserver.response.ObjectResponse
import com.comet.mudleserver.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
class MusicController(val musicService: MusicService) {

    //가공해서 response로
    @PostMapping("/request")
    fun request(@RequestBody musicRequestDTO: MusicRequestDTO) : CompletableFuture<ResponseEntity<Response>> {
        return musicService.request(musicRequestDTO.uuid, musicRequestDTO.url)
    }

    @GetMapping("/music")
    fun music() : ObjectResponse<MusicResponseDTO> {
        musicService.updateTimeMusic()
        return ObjectResponse("OK", MusicResponseDTO(musicService.music))
    }
}