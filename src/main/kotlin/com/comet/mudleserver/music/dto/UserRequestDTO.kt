package com.comet.mudleserver.music.dto

import com.comet.mudleserver.music.model.User
import java.util.UUID

data class UserRequestDTO(val name : String, val uuid: UUID) {

    fun toEntity(coin : Int) : User {
        return User(null, name, uuid, coin)
    }
}