package com.comet.mudleserver.music.dto

import com.comet.mudleserver.music.model.User
import com.comet.mudleserver.uuid
import java.util.UUID

data class UserResponseDTO(val name : String?, val uuid: UUID?, val coin : Int?) {

    constructor(user : User) : this(user.name, user.uuid, user.coin)

}