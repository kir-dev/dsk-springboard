package hu.bme.dsk.rentings

import hu.bme.dsk.equipments.EquipmentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EquipmentRentingRepository : JpaRepository<EquipmentEntity, Long> {

}