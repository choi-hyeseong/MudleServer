package com.comet.mudleserver.music.service

import com.comet.mudleserver.music.dto.UserRequestDTO
import com.comet.mudleserver.music.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class UserServiceTest {

    @Test
    @DisplayName("유저 생성 테스트")
    fun CREATE_USER() {
        val userRepository : UserRepository = Mockito.mock(UserRepository::class.java)
        val userService = UserService(userRepository)
        val user = UserRequestDTO("test", UUID.randomUUID())
        val response = userService.createUser(user)
        assertEquals(user.name, response.name)
        assertEquals(user.uuid, response.uuid)
        assertEquals(response.coin, 10) //10으로 설정

    }
}