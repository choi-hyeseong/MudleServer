package com.comet.mudleserver.music.repository

import com.comet.mudleserver.music.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, Long> {

    fun findByUuid(uuid: UUID) : User
}