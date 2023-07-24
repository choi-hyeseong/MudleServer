package com.comet.mudleserver.music.model

import com.comet.mudleserver.music.type.MessageType
import lombok.AllArgsConstructor

@AllArgsConstructor
data class Message(val type : MessageType, val sender : String, val content : String)