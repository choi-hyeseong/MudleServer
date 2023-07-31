package com.comet.mudleserver.music.model

import com.comet.mudleserver.uuid
import java.util.UUID

//더 추가될 수 있음
data class Music(val link : String, var startTime : Long, val title : String, var isPlaying : Boolean, var isAnswered : Boolean, var requester : String, var requesterUUID: UUID) {

    companion object {
        fun empty() : Music {
            return Music("", 0, "", isPlaying = false, isAnswered = false, requester = "", requesterUUID = uuid)
        }
    }
}