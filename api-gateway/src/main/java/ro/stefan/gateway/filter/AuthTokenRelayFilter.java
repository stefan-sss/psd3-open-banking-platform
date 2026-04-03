package ro.stefan.gateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import ro.stefan.gateway.ApiGatewayApplication;
import ro.stefan.gateway.config.ApiGatewayConfig;

public class AuthTokenRelayFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);
		
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();

        if (!authHeaders.isEmpty()) {
            builder.header(HttpHeaders.AUTHORIZATION, authHeaders.get(0));
        }

        return chain.filter(exchange.mutate().request(builder.build()).build());
		
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return ApiGatewayConfig.AUTH_TOKEN_RELAY_FILTER_ORDER;
	}

}
