package hu.bme.dsk.rentings

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RentingRepository : JpaRepository<RentingEntity, Long> {

}