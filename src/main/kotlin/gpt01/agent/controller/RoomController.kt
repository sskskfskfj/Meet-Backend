package gpt01.agent.controller

import gpt01.agent.dto.room.CreateRoomDto
import gpt01.agent.dto.ResponseDto
import gpt01.agent.dto.room.DeleteRoomDto
import gpt01.agent.dto.room.JoinRoomDto
import gpt01.agent.dto.room.LeaveRoomDto
import gpt01.agent.entity.RoomEntity
import gpt01.agent.service.RoomService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/room")
class RoomController(
    private val roomService: RoomService,
) {
    private val logger = LoggerFactory.getLogger(RoomController::class.java)

    /*
    room 생성 controller
    roomRequest에 username붙여서 service로 전송
     */
    @PostMapping("/create")
    fun createRoom(
        @RequestBody roomRequest : CreateRoomDto,
    ): ResponseEntity<ResponseDto<String>> {
        try{
            logger.info("room create request ${roomRequest.roomName}, ${roomRequest.createdUser}")
            val result = roomService.createRoom(roomRequest)
            return ResponseEntity.ok().body(result)

        }catch (e: Exception){
            logger.error(e.message)
            return ResponseEntity.badRequest().body(
                ResponseDto("error creating room")
            )
        }
    }

    /*
    참가한 사용자 데이터 관리
     */
    @PostMapping("/join")
    fun joinRoom(@RequestBody joinRoomRequest : JoinRoomDto): ResponseEntity<ResponseDto<String>> {
        logger.info("join request ${joinRoomRequest.roomName}, ${joinRoomRequest.joinUser}")
        try{
            return ResponseEntity.ok().body(roomService.joinRoom(joinRoomRequest))
        }catch (e : Exception){
            logger.error(e.message)
            return ResponseEntity.badRequest().body(
                ResponseDto("error joining room")
            )
        }
    }
    /*
    나간 사용자 데이터 관리
     */
    @PostMapping("/leave")
    fun leaveRoom(@RequestBody leaveRoomRequest : LeaveRoomDto) : ResponseEntity<ResponseDto<String>> {
        try{
            logger.info("leave request")
            return ResponseEntity.ok().body(roomService.leaveRoom(leaveRoomRequest))
        }catch (e: Exception){
            logger.error(e.message)
            return ResponseEntity.badRequest().body(
                ResponseDto("error leaving room")
            )
        }
    }

    @PostMapping("/delete")
    fun deleteRoom(@RequestBody deleteRoomRequest: DeleteRoomDto) : ResponseEntity<ResponseDto<String>> {
        logger.info("delete request")
        return ResponseEntity.ok().body(roomService.deleteRoom(deleteRoomRequest))
    }

    /*
    room 조회, ResponseEntity로 리턴
     */
    @GetMapping("/all")
    fun getAllRooms() : ResponseEntity<ResponseDto<List<RoomEntity>>>{
        logger.info("getAllRoom request")
        val result = roomService.getAllRooms()

        result.message.forEach { rooms ->
            logger.info(rooms.roomName)
        }

        return ResponseEntity.ok().body(result)
    }


}