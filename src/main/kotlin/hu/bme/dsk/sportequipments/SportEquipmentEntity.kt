package hu.bme.dsk.sportequipments

import hu.bme.dsk.rentings.SportEquipmentRentingEntity
import hu.bme.dsk.sports.SportEntity
import jakarta.persistence.*

@Entity
@Table(name = "sport_equipments")
data class SportEquipmentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    var name: String,

    var description: String,

    var count: Int,

    var imageLink: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", nullable = false)
    var sportId: SportEntity,

    @OneToMany(mappedBy = "sport_equipment", cascade = [CascadeType.ALL], orphanRemoval = true)
    var equipmentRenting: MutableList<SportEquipmentRentingEntity> = mutableListOf(),
) {
    override fun toString(): String {
        return this::class.java.simpleName + "(id = $id, name = $name, description = $description, count = $count)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SportEquipmentEntity) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
