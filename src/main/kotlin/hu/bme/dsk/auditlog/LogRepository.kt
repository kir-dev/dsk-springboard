package hu.bme.dsk.auditlog

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogRepository : JpaRepository<LogEntity, Long> {
}