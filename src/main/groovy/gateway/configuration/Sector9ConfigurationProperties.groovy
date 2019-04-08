package gateway.configuration

import gateway.model.Rute
import groovy.transform.Canonical
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Component
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.gateway")
@Canonical
class Sector9ConfigurationProperties {
    List<Rute> routes = []
}
