package gpt01.agent.repository

import gpt01.agent.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
}