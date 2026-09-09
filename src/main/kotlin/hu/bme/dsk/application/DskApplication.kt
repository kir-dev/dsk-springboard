package hu.bme.dsk.application

import hu.bme.dsk.config.StartupPropertyConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableConfigurationProperties(value = [StartupPropertyConfig::class])

@SpringBootApplication(scanBasePackages = ["hu.bme.dsk"])
@EnableJpaRepositories(basePackages = ["hu.bme.dsk"])
@EntityScan(basePackages = ["hu.bme.dsk"])
class DskApplication

fun main(args: Array<String>) {
	runApplication<DskApplication>(*args)
}
