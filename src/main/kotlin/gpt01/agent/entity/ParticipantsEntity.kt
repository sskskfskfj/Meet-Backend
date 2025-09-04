package gpt01.agent.entity

import jakarta.persistence.*

@Entity
class ParticipantsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L,

    @ManyToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "user_id", nullable = false)
    val user : UserEntity,

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