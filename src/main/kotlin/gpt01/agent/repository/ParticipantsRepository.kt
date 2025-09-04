package gpt01.agent.repository

import gpt01.agent.entity.ParticipantsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantsRepository : JpaRepository<ParticipantsEntity, Long> {
}