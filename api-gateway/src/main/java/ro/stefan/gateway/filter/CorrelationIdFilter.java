package ro.stefan.gateway.filter;

import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class CorrelationIdFilter  implements GlobalFilter, Ordered{

	public static final String HEADER_NAME = "X-Correlation-Id";
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String correlationId =  exchange.getRequest().getHeaders().getFirst(HEADER_NAME);
		if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
	    }

		ServerHttpRequest mutatedRequest = exchange.getRequest()
	                .mutate()
	                .header(HEADER_NAME, correlationId)
	                .build();

		ServerWebExchange mutatedExchange = exchange.mutate()
	                .request(mutatedRequest)
	                .build();

	    return chain.filter(mutatedExchange);
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -100;
	}
}
