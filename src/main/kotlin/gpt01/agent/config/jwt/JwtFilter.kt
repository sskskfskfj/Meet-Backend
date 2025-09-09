package gpt01.agent.config.jwt

import java.util.Collections
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if ("OPTIONS" == request.method) {
            response.status = HttpServletResponse.SC_OK
            filterChain.doFilter(request, response)
            return
        }
        val header = request.getHeader("Authorization")
        if(header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)
            if(jwtUtil.isTokenValid(token)) {
                try{
                    logger.info("jwt filter : authentication success")
                    val username = jwtUtil.parseToken(token)["username"] as String
                    logger.info("username : $username")
                    val authentication = UsernamePasswordAuthenticationToken(
                        username, null,
                        Collections.singleton(SimpleGrantedAuthority("ROLE_USER"))
                    )
                    SecurityContextHolder.getContext().authentication = authentication


                }catch (e : Exception) {
                    logger.error(e.message)
                    SecurityContextHolder.clearContext()
                }

            }
        }
        filterChain.doFilter(request, response)
    }
}