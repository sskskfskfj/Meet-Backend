package gpt01.agent.controller

import gpt01.agent.dto.ResponseDto
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth2")
class AuthController {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<ResponseDto<String>> {
        logger.info("logout request")
        val cookie = ResponseCookie.from("access-token", "")
            .httpOnly(false)
            .secure(true) // 배포 시 true
            .sameSite("None")
            .path("/")
            .maxAge(0)
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
        return ResponseEntity.ok().body(
            ResponseDto("logged out")
        )
    }

}