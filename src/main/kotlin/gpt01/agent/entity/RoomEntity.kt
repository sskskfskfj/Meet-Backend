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

    @Column(nullable = false, name = "maxParticipants")
    var maxParticipants : Int,

    @Version
    @Column(nullable = false, name = "currentParticipants")
    var currentParticipants : Int = 1,

    @Column(name = "created")
    val createdTime : Long = System.currentTimeMillis(),
){
    companion object{
        fun to(
            roomName : String,
            maxParticipants : Int
        ) : RoomEntity = RoomEntity(roomName = roomName, maxParticipants = maxParticipants)
    }
}