package hu.bme.dsk.application

import hu.bme.dsk.config.StartupPropertyConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(value = [StartupPropertyConfig::class])

@SpringBootApplication
class DskApplication

fun main(args: Array<String>) {
	runApplication<DskApplication>(*args)
}
