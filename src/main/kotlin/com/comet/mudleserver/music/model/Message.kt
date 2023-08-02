package com.comet.mudleserver.music.model

import com.comet.mudleserver.music.type.MessageType
import lombok.AllArgsConstructor
import java.util.UUID

@AllArgsConstructor
data class Message(val type: MessageType, val uuid: UUID, val sender: String, val message: String, val timeStamp : Long) {
    companion object {
        fun makeSystemMessage(type: MessageType, message: String): Message {
            return Message(type, com.comet.mudleserver.uuid, "System", message, System.currentTimeMillis())
        }
    }
}