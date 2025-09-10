package gpt01.agent.service

import gpt01.agent.dto.room.CreateRoomDto
import gpt01.agent.dto.ResponseDto
import gpt01.agent.dto.room.DeleteRoomDto
import gpt01.agent.dto.room.JoinRoomDto
import gpt01.agent.dto.room.LeaveRoomDto
import gpt01.agent.entity.ParticipantsEntity
import gpt01.agent.entity.RoomEntity
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

    /*
    room 생성 (user <- participants -> room)
     */
    @Transactional
    fun createRoom(request : CreateRoomDto) : ResponseDto<String> {
        val username = request.createdUser
        val roomName = request.roomName

        logger.info("Creating room $roomName by $username")
        if(roomRepository.findRoomEntityByRoomName(roomName) == null){
            val user = userRepository.findByUsername(username)!!
            val room : RoomEntity = RoomEntity.to(roomName, request.maxParticipants, username)

            logger.info("roomName : ${room.roomName}")
            roomRepository.save(room)
            participantsRepository.save(ParticipantsEntity.to(user, room))

            return ResponseDto(username)
        }else{
            return ResponseDto("room $roomName already exists")
        }
    }

    /*
    방 참가
    mapping table에 사용자와 방이 존재하면 -> 이미 참가
    그게 아니라면 mappinng table에 데이터를 넢는다
     */
    @Transactional
    fun joinRoom(request : JoinRoomDto) : ResponseDto<String> {
        val user = userRepository.findByUsername(request.joinUser)
        val room = roomRepository.findRoomEntityByRoomName(request.roomName) ?: return ResponseDto("Room not found")

        if(participantsRepository.countByRoom(room) >= room.maxParticipants){
            return ResponseDto("full!")
        }
        room.currentParticipants++

        if(participantsRepository.findByUserAndRoom(user!!, room) != null){
            return ResponseDto("already participate in room ${request.roomName}")
        }else{
            participantsRepository.save(ParticipantsEntity.to(user, room))
            return ResponseDto("room ${request.roomName} joined")
        }
    }

    /*
    방 나가기
    mapping table에서의 데이터 삭제, room에서의 인원 -1
     */
    @Transactional
    fun leaveRoom(request : LeaveRoomDto) : ResponseDto<String> {
        val user = userRepository.findByUsername(request.leaveUser)
        val room = roomRepository.findRoomEntityByRoomName(request.roomName)
        val participants = participantsRepository.findByUserAndRoom(user!!, room!!)

        if(participants == null){
            return ResponseDto("already leave room")
        }else{
            room.currentParticipants--

            roomRepository.save(room)
            participantsRepository.delete(participants)
            return ResponseDto("room ${request.roomName} left")
        }
    }

    @Transactional
    fun deleteRoom(request : DeleteRoomDto) : ResponseDto<String> {
        val room = roomRepository.findRoomEntityByRoomName(request.roomName)

        if(participantsRepository.findByRoom(room!!) == null && room.createdUser == request.createUser){
            roomRepository.delete(room)
            return ResponseDto("delete room ${request.roomName}")
        }else {
            return ResponseDto("user exists")
        }
    }

    /*
    모든 room 조회
     */
    @Transactional(readOnly = true)
    fun getAllRooms() : ResponseDto<List<RoomEntity>> = ResponseDto(roomRepository.findAll())


}