package com.comet.mudleserver.music.model

//더 추가될 수 있음
data class Music(val link : String, var startTime : Long, val title : String, var isPlaying : Boolean) {

    companion object {
        fun empty() : Music {
            return Music("", 0, "", false)
        }
    }
}