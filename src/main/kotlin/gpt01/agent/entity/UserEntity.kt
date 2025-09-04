package gpt01.agent.entity

import jakarta.persistence.*

@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L,

    @Column(name = "username", nullable = false)
    val username : String = "",

    @Column(name = "email", nullable = false)
    val email : String = "",

    @Column(name = "password")
    val password : String = "",

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = [(CascadeType.ALL)])
    var participatedRoom : MutableList<ParticipantsEntity> = mutableListOf(),

){
    companion object {
        /*
        추후에 파라미터 dto로 변경
         */
        fun toEntity(username : String, email : String, password: String) : UserEntity {
            return UserEntity(
                username = username,
                email = email,
                password = password
            )
        }
    }

}