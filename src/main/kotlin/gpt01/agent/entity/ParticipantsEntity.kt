package gpt01.agent.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class ParticipantsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L,

    @JsonIgnore
    @ManyToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "user_id", nullable = false)
    val user : UserEntity,

    @JsonIgnore
    @ManyToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "participant_id", nullable = false)
    val room : RoomEntity

) {
    companion object {
        fun to(user : UserEntity, room : RoomEntity) : ParticipantsEntity {
            return ParticipantsEntity(
                user = user,
                room = room,
            )
        }
    }
}