package gateway.filters

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR

@Order(-2)
@Component
class LoggingFilter implements GlobalFilter {
    Log log = LogFactory.getLog(getClass())

    @Override
    Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR)
        log.info "Kald til: '${exchange.request.URI}' videresendes til: '${route.uri}' for ruten: '${route.id}'"
        return chain.filter(exchange)
    }
}