package hu.bme.dsk.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding


@ConfigurationProperties(prefix = "hu.bme.dsk.startup")
data class StartupPropertyConfig @ConstructorBinding constructor(

    val sysadmins: String,

    // JWT
    val secretKey: String,
    val sessionValiditySeconds: Long,

) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun onInit() {
        log.info("StartupPropertyConfig settings: {}", this.toString())
    }

}