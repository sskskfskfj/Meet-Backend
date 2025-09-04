package gpt01.agent.service

import gpt01.agent.dto.ResponseDto
import io.livekit.server.AccessToken
import io.livekit.server.RoomJoin
import io.livekit.server.RoomName
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class LiveKitService {

    @Value("\${LIVEKIT_API_KEY}")
    private lateinit var apiKey : String

    @Value("\${LIVEKIT_API_SECRET}")
    private lateinit var apiSecret : String

    /*
    livekit 접근용 jwt 생성 username, roomName 사용
     */
    fun createToken(params : Map<String, String>) : ResponseDto<Map<String, String>> {
        val roomName = params["roomName"]
        val participant = params["participant"]

        if(roomName == null || participant == null) {
            return ResponseDto(mapOf("error" to "roomName and participants cannot be empty"))
        }

        val accessToken = AccessToken(apiKey, apiSecret)
        accessToken.name = participant
        accessToken.identity = participant
        accessToken.addGrants(RoomJoin(true), RoomName(roomName))

        return ResponseDto(mapOf("token" to accessToken.toJwt()))
    }
}