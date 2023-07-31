package com.comet.mudleserver.music.controller

import com.comet.mudleserver.music.dto.UserRequestDTO
import com.comet.mudleserver.music.dto.UserResponseDTO
import com.comet.mudleserver.music.service.UserService
import com.comet.mudleserver.response.ObjectResponse
import com.comet.mudleserver.response.Response
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/user") //이렇게 해야함..
class UserController(val userService: UserService) {

    //나중에 post
    @PostMapping("")
    fun register(@RequestBody userRequestDTO: UserRequestDTO) : ResponseEntity<Response> {
        userService.createUser(userRequestDTO)
        return ResponseEntity(Response("회원가입이 성공적으로 진행되었습니다."), HttpStatus.OK)
    }

    @GetMapping("")
    fun user(@RequestParam uuid: UUID) : ResponseEntity<ObjectResponse<UserResponseDTO>> {
        val user = userService.getUser(uuid)
        val code : HttpStatusCode
        val result : String
        if (user != null) {
            result = "Success"
            code = HttpStatus.OK
        }
        else {
            result = "Error"
            code = HttpStatus.BAD_REQUEST
        }
        return ResponseEntity(ObjectResponse(result, user), code)
    }
}