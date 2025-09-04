package gpt01.agent.service

import gpt01.agent.dto.auth.CustomOauth2User
import gpt01.agent.entity.UserEntity
import gpt01.agent.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomOauth2UserService(
    private val userRepository: UserRepository,
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private val logger = LoggerFactory.getLogger(CustomOauth2UserService::class.java)

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val oauth2UserService = DefaultOAuth2UserService()
        val user = CustomOauth2User(oauth2UserService.loadUser(userRequest).attributes)

        val loginUser : UserEntity? = userRepository.findByUsername(user.name)
        if(loginUser == null) {
            val newUser = UserEntity.toEntity(user.name, user.getEmail(), "login by oauth2")
            logger.info("created user : {}", newUser.username)
            userRepository.save(newUser)

        }
        return user
    }
}