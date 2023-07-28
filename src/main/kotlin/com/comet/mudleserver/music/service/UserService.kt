package com.comet.mudleserver.music.service

import com.comet.mudleserver.music.dto.UserRequestDTO
import com.comet.mudleserver.music.dto.UserResponseDTO
import com.comet.mudleserver.music.repository.UserRepository
import com.comet.mudleserver.response.Response
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    fun createUser(userRequestDTO: UserRequestDTO) : UserResponseDTO{
        return userRequestDTO.toEntity(10).let {
            userRepository.save(it)
            UserResponseDTO(it)
        }
    }

    fun getUser(uuid: UUID) : UserResponseDTO {
        return UserResponseDTO(userRepository.findByUuid(uuid))
    }
}