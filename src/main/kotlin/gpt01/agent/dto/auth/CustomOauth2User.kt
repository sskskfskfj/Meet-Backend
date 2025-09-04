package gpt01.agent.dto.auth

import gpt01.agent.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.*

class CustomOauth2User(
//    private val user : UserEntity,
    private val attributes : MutableMap<String, Any>,
) : OAuth2User {
    override fun getName(): String = attributes["name"] as String
    override fun getAttributes(): MutableMap<String, Any> = attributes
    fun getEmail(): String = attributes["email"] as String

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.singleton(GrantedAuthority { "ROLE_USER" })
    }

}