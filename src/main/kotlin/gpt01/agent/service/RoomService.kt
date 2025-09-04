package gpt01.agent.service

import gpt01.agent.dto.CreateRoomDto
import gpt01.agent.dto.ResponseDto
import gpt01.agent.entity.ParticipantsEntity
import gpt01.agent.repository.ParticipantsRepository
import gpt01.agent.repository.RoomRepository
import gpt01.agent.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoomService (
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val participantsRepository: ParticipantsRepository
){
    private val logger = LoggerFactory.getLogger(RoomService::class.java)

    @Transactional
    fun createRoom(request : CreateRoomDto) : ResponseDto<String> {
        val username = request.createdUser
        val roomName = request.roomName
        logger.info("Creating room $roomName by $username")

        val room = roomRepository.findRoomEntityByRoomName(roomName)?: return ResponseDto("Room not found")
        val user = userRepository.findByUsername(username)!!

        participantsRepository.save(ParticipantsEntity.to(user, room))

        return ResponseDto("room $roomName created by $username")
    }


}