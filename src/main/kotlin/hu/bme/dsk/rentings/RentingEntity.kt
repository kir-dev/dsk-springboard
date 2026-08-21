package hu.bme.dsk.rentings


import hu.bme.dsk.users.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "renting")
data class RentingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: Instant,

    var startTime: Instant,

    var endTime: Instant,

    @ManyToOne(fetch = FetchType.LAZY)
    var creatingUser: UserEntity,

    var rentingStatus: RentingStatus,

    @OneToMany(mappedBy = "renting", cascade = [CascadeType.ALL], orphanRemoval = true)
    var equipmentRenting: MutableList<EquipmentRentingEntity> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    var issuingUser: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    var returningUser: UserEntity,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RentingEntity) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return this::class.simpleName + "(id = $id , startTime = $startTime , endTime = $endTime , creatingUser = $creatingUser , issuingUser = $issuingUser , returningUser = $returningUser )"
    }
}
