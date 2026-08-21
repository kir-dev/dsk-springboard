package hu.bme.dsk.rentings

import hu.bme.dsk.sportequipments.SportEquipmentEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.PositiveOrZero

@Entity
@Table(name = "sport_equipments_renting")
data class SportEquipmentRentingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    @PositiveOrZero
    var count: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    var equipment: SportEquipmentEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    var renting: RentingEntity,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SportEquipmentRentingEntity) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}