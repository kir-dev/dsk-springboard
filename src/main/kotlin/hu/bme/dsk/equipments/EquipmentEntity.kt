package hu.bme.dsk.equipments

import hu.bme.dsk.rentings.EquipmentRentingEntity
import hu.bme.dsk.sports.SportEntity
import jakarta.persistence.*

@Entity
@Table(name = "equipments")
data class EquipmentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    var name: String,

    var description: String,

    var count: Int,

    var availableCount: Int,

    var imageLink: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", nullable = false)
    var sport: SportEntity,

    @OneToMany(mappedBy = "sport_equipment", cascade = [CascadeType.ALL], orphanRemoval = true)
    var equipmentRenting: MutableList<EquipmentRentingEntity> = mutableListOf(),
) {
    override fun toString(): String {
        return this::class.java.simpleName + "(id = $id, name = $name, description = $description, count = $count)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EquipmentEntity) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
