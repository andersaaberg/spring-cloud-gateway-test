package gateway

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.HttpStatus
import org.springframework.http.client.support.BasicAuthenticationInterceptor
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0) // benyt random port
class FilterSpec extends Specification {

    @LocalServerPort
    int springBootPort

    RestTemplate restTemplate = new RestTemplate()

    void "test simpel gateway"() {
        // TODO: skal have mocket endpointet som sector9 kalder over på

        when:
        String response = restTemplate.getForObject("http://localhost:${springBootPort}/simpel", String.class)

        then:
        response == "{\"svar\":\"Hello World\"}"
    }

    void "test rute med sikkerhed - afvises pga. manglende login"() {
        when:
        restTemplate.getForObject("http://localhost:${springBootPort}/sikkerhed/gateway1", String.class)

        then:
        HttpClientErrorException exception = thrown()
        exception.statusCode == HttpStatus.UNAUTHORIZED
    }

    void "test rute med sikkerhed - login lykkes"() {
        given:
        // TODO: skal have mocket endpointet som sector9 kalder over på
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor('Anders', 'And'))

        when:
        String response = restTemplate.getForObject("http://localhost:${springBootPort}/sikkerhed/gateway1", String.class)

        then:
        response == "{\"svar\":\"Hello World\"}"
    }
}