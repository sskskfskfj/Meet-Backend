package gpt01.agent.repository

import gpt01.agent.entity.ParticipantsEntity
import gpt01.agent.entity.RoomEntity
import gpt01.agent.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ParticipantsRepository : JpaRepository<ParticipantsEntity, Long> {

    @Query("select p from ParticipantsEntity p where p.user = :user and p.room = :room")
    fun findByUserAndRoom(
        @Param("user") user: UserEntity,
        @Param("room") room: RoomEntity
    ): ParticipantsEntity?

    fun findByUser(user : UserEntity) : ParticipantsEntity?
    fun findByRoom(room : RoomEntity) : ParticipantsEntity?
    fun countByRoom(room : RoomEntity) : Int
}