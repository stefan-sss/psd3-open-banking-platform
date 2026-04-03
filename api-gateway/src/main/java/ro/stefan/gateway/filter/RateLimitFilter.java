package ro.stefan.gateway.filter;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import reactor.core.publisher.Mono;
import ro.stefan.gateway.config.ApiGatewayConfig;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered{
    
	private final Cache<String, AtomicInteger> requestCounters;
    private final int maxRequestsPerMinute;
    
    public RateLimitFilter(@Value("${gateway.rate-limit.max-requests-per-minute:60}") int maxRequestsPerMinute)  {
        this.maxRequestsPerMinute = maxRequestsPerMinute;
        this.requestCounters = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).maximumSize(10_000).build();
	}
    
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		if(!shoudRateLimit(path)) {
			return chain.filter(exchange);
		}
		String clientKey = resolveClientKey(exchange);
        AtomicInteger counter = requestCounters.get(clientKey, key -> new AtomicInteger(0));
        int current = counter.incrementAndGet();

        if (current > maxRequestsPerMinute) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);

		
	}

	private boolean shoudRateLimit(String path) {
		return path.startsWith("/api/payment")
				|| path.startsWith("/api/accounts")
				|| path.startsWith("/api/dispute");
	}
	
	private String resolveClientKey(ServerWebExchange exchange) {
        String forwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor;
        }

        if (exchange.getRequest().getRemoteAddress() != null &&
                exchange.getRequest().getRemoteAddress().getAddress() != null) {
            return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }

        return "unknown-client";
	}
	
	@Override
	public int getOrder() {
		return ApiGatewayConfig.RATE_LIMIT_FILTER;
	}

}
