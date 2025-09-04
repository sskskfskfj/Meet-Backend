package gpt01.agent.entity

import jakarta.persistence.*

@Entity
class RoomEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L,

    @Column(nullable = false, name = "roomName")
    val roomName : String = "",

    @OneToMany(mappedBy = "room", orphanRemoval = true, cascade = [(CascadeType.ALL)])
    val participants : MutableList<ParticipantsEntity> = mutableListOf(),
)