package gpt01.agent.config.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.time.LocalDateTime
import java.util.*

@Component
class JwtUtil {
    @Value("\${jwt.secret-key}")
    private lateinit var key : String
    private val expiration : Long = 60000L
    private lateinit var secretKey : Key

    @PostConstruct
    fun init() {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key.toByteArray()))
    }
    /*
    jwt 생성 로직 username, email기반
     */
    fun generateToken(username : String, email : String) : String {
        val now = System.currentTimeMillis()
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date(now))
            .setExpiration(Date(now + expiration))
            .addClaims(
                mapOf(
                    "username" to username,
                    "email" to email,
                )
            )
            .signWith(this.secretKey)
            .compact()
    }

    /*
    jwt에서 username, email 추출
     */
    fun parseToken(token : String) : Map<String, Any?> {
        val claims = Jwts.parserBuilder()
            .setSigningKey(this.secretKey)
            .build()
            .parseClaimsJws(token)
            .body
        return mapOf(
            "username" to claims["username"],
            "email" to claims["email"]
        )
    }

    /*
    토큰 유효성 검사
     */
    fun isTokenValid(token : String) : Boolean {
        if(token.isBlank()) return false
        return try {
            Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e : Exception) {
            false
        }
    }
}