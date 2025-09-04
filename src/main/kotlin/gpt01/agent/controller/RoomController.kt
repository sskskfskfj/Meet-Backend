package gpt01.agent.controller

import gpt01.agent.dto.CreateRoomDto
import gpt01.agent.dto.ResponseDto
import gpt01.agent.dto.auth.CustomOauth2User
import gpt01.agent.service.RoomService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/room")
class RoomController(
    private val roomService: RoomService,
) {
    private val logger = LoggerFactory.getLogger(RoomController::class.java)

    @PostMapping("/create")
    fun createRoom(
        @RequestBody roomRequest : CreateRoomDto,
    ): ResponseEntity<ResponseDto<String>> {
        try{
            logger.info("room create request")
            val authentication = SecurityContextHolder.getContext().authentication.principal as CustomOauth2User
            roomRequest.createdUser = authentication.name

            val result = roomService.createRoom(roomRequest)
            return ResponseEntity.ok().body(result)

        }catch (e: Exception){
            logger.error(e.message)
            return ResponseEntity.badRequest().body(
                ResponseDto("error creating room")
            )
        }

    }
}