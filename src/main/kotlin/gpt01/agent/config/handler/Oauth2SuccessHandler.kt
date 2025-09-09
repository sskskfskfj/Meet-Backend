package gpt01.agent.config.handler

import gpt01.agent.config.jwt.JwtUtil
import gpt01.agent.dto.auth.CustomOauth2User
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class Oauth2SuccessHandler(
    private val jwtUtil: JwtUtil,
) : SimpleUrlAuthenticationSuccessHandler() {

    /*
    oauth2 로그인 성공했을 때 프론트에 jwt 전송
     */
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ){
        logger.info("from successHandler")
        val user = authentication.principal as CustomOauth2User
        val accessToken : String = jwtUtil.generateToken(user.name)
        logger.info(accessToken)

        val cookie = ResponseCookie.from("access-token", accessToken)
            .httpOnly(false)
            .secure(true)       // openssl 인증서 적용
            .sameSite("None")
            .path("/")
            .maxAge(Duration.ofMinutes(10))
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
        response.sendRedirect("https://localhost:5173")

    }
}