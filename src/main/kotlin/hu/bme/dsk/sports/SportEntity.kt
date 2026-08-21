package hu.bme.dsk.sports

import jakarta.persistence.*
import org.hibernate.validator.constraints.UUID
import kotlin.uuid.Uuid

@Entity
@Table(name = "sports")
data class SportEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    var name: String,

    @OneToMany(mappedBy = "sport", cascade = [CascadeType.ALL], orphanRemoval = true)
    var sportEquipments: MutableList<SportEntity> = mutableListOf(),
) {
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return this::class.toString() + "(id = $id , name = $name )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SportEntity) return false
        if (id != other.id) return false
        return true
    }
}
