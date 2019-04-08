package gateway

import gateway.configuration.Sector9ConfigurationProperties
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(Sector9ConfigurationProperties.class)
class Application {

    static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }
}