package gateway.filters

import gateway.configuration.Sector9ConfigurationProperties
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Order(-1) // sikkerhed skal være det første der tjekkes
@Component
class SikkerhedFilter implements GlobalFilter {

    Log log = LogFactory.getLog(getClass())

    @Autowired
    Sector9ConfigurationProperties sector9ConfigurationProperties

    @Override
    Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR)
        String sikkerhed = sector9ConfigurationProperties.routes.find { it.id == route.id }?.sikkerhed

        if(sikkerhed) {
            log.info("Sikkerhed er sat til ${sikkerhed} for ruten ${route.id}")

            if((sikkerhed == 'BASIC AUTH') && !loginBasicAuth(exchange.request.headers)) {
                ServerWebExchangeUtils.setResponseStatus(exchange, HttpStatus.UNAUTHORIZED)
                return Mono.empty()
            }
        }
        return chain.filter(exchange)
    }

    boolean loginBasicAuth(HttpHeaders httpHeaders) {
        List<String> values = httpHeaders.get(HttpHeaders.AUTHORIZATION)
        if (CollectionUtils.isNotEmpty(values)) {
            String autorizationValue = values.get(0)

            if (StringUtils.isNotBlank(autorizationValue)) {
                String[] tokens = autorizationValue.split(" ")

                if (tokens != null && tokens.length == 2 && "basic".equalsIgnoreCase(tokens[0])) {

                    String[] auths = null

                    try {
                        auths = new String(Base64.getDecoder().decode(tokens[1])).split(":")
                    } catch (IllegalArgumentException e) {
                        /*
                         * Ignore
                         */
                    }

                    if (auths != null && auths.length == 2 && auths[0] == 'Anders' && auths[1] == 'And') {
                        log.info auths[0]
                        log.info auths[1]
                        return true
                    }
                }
            }
        }

        return false
    }

}
