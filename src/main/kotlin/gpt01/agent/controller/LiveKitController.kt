package gpt01.agent.controller

import gpt01.agent.dto.LiveKitRequestDto
import gpt01.agent.dto.ResponseDto
import gpt01.agent.service.LiveKitService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/livekit")
class LiveKitController(
    private val liveKitService: LiveKitService,
) {
    private val logger = LoggerFactory.getLogger(LiveKitController::class.java)
    /*
    livekit에 접근할 때 사용할 jwt 생성 로직
     */
    @PostMapping("/token")
    fun getToken(@RequestBody params: LiveKitRequestDto): ResponseEntity<Map<String, String>> {
        logger.info("request from user")
        logger.info("request user : {}", SecurityContextHolder.getContext().authentication.name)
        val request = mapOf(
            "roomName" to params.roomName,
            "participant" to params.username
        )
        val token : Map<String, String> = liveKitService.createToken(request).message
        logger.info("created token: $token")
        return ResponseEntity.ok().body(token)
    }


}