package gpt01.agent.repository

import gpt01.agent.entity.RoomEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RoomRepository : JpaRepository<RoomEntity, Long> {
    fun findRoomEntityByRoomName(roomName: String): RoomEntity?
}