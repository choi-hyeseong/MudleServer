package com.comet.mudleserver.music.service

import com.comet.mudleserver.music.dto.UserRequestDTO
import com.comet.mudleserver.music.dto.UserResponseDTO
import com.comet.mudleserver.music.model.User
import com.comet.mudleserver.music.repository.UserRepository
import com.comet.mudleserver.response.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class UserService(private val userRepository: UserRepository) {

    @Value("\${music.creation.coin}")
    var creationCoin : Int = 0

    fun createUser(userRequestDTO: UserRequestDTO) {
        userRepository.save(userRequestDTO.toEntity(creationCoin))
    }

    @Transactional
    fun getUser(uuid: UUID): UserResponseDTO? {
        val result = userRepository.findByUuid(uuid)
        return if (result.isEmpty)
            null
        else
            UserResponseDTO(result.get())
    }

    @Transactional
    fun isUserExists(uuid: UUID) : Boolean {
        return userRepository.existsByUuid(uuid)
    }

    @Transactional
    fun consumeCoin(uuid: UUID, amount : Int) {
        userRepository.save(userRepository.findByUuid(uuid).get().apply {
            coin = coin?.minus(amount)
        })
    }

    @Transactional
    fun giveCoin(uuid: UUID, amount : Int) {
        userRepository.save(userRepository.findByUuid(uuid).get().apply {
            coin = coin?.plus(amount)
        })
    }
}