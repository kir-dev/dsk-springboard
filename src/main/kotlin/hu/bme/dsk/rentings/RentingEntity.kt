package hu.bme.dsk.rentings


import hu.bme.dsk.sportequipments.SportEquipmentEntity
import hu.bme.dsk.rentings.SportEquipmentRentingEntity
import hu.bme.dsk.users.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import kotlin.time.Instant

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
    var equipmentRenting: MutableList<SportEquipmentRentingEntity> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    var issuingUser: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    var returningUser: UserEntity,
)
