package hu.bme.dsk.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DskApplication

fun main(args: Array<String>) {
	runApplication<DskApplication>(*args)
}
