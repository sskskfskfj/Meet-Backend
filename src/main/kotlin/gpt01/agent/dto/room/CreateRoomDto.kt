package gpt01.agent.dto.room

data class CreateRoomDto(
    val roomName: String,
    var createdUser : String,
    val maxParticipants : Int,
)