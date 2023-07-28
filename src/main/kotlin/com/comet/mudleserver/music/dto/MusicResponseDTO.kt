package com.comet.mudleserver.music.dto

import com.comet.mudleserver.music.model.Music

data class MusicResponseDTO(val link : String, val startTime : Long, val isPlaying : Boolean) {

    constructor(music : Music) : this(music.link, music.startTime, music.isPlaying)
}