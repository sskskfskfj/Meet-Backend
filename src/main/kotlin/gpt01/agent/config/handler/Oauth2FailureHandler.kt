package gpt01.agent.config.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class Oauth2FailureHandler(

) : SimpleUrlAuthenticationFailureHandler(){

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        logger.info("Authentication failed")
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed")
    }
}