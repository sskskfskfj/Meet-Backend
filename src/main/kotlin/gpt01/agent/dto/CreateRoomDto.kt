package gpt01.agent.dto

data class CreateRoomDto(
    val roomName: String,
    var createdUser : String = "",
)