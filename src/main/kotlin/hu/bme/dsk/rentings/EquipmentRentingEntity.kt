package hu.bme.dsk.rentings

import hu.bme.dsk.equipments.EquipmentEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.PositiveOrZero
import java.util.UUID

@Entity
@Table(name = "equipments_renting")
data class EquipmentRentingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @PositiveOrZero
    var count: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    var equipment: EquipmentEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renting_id", nullable = false)
    var renting: RentingEntity,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EquipmentRentingEntity) return false
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