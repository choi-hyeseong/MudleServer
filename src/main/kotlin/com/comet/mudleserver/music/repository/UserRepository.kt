package com.comet.mudleserver.music.repository

import com.comet.mudleserver.music.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UserRepository : JpaRepository<User, Long> {

    fun findByUuid(uuid: UUID) : Optional<User>

    fun existsByUuid(uuid: UUID) : Boolean

}