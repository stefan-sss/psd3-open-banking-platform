package ro.stefan.gateway.filter;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import ro.stefan.gateway.config.ApiGatewayConfig;

@Component
public class RequestLoggingFilter implements GlobalFilter,Ordered{

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long start = System.currentTimeMillis();
        
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethod().name();
        String path = request.getURI().getPath();
        String correlationId= request.getHeaders().getFirst(CorrelationIdFilter.HEADER_NAME);
        
        LOGGER.info("Incoming request method={} path={} correlationId={}", method, path, correlationId);
        
        return chain.filter(exchange).doFinally(new Consumer<SignalType>() {
			@Override
			public void accept(SignalType signalType) {

				long durationMs = System.currentTimeMillis()-start;
				int status = exchange.getResponse().getStatusCode() !=null
						? exchange.getResponse().getStatusCode().value():0;        
				
				LOGGER.info("Outgoing response method={} path={} status={} durationMs={} correlationId={}",
						method,path,status,durationMs,status);
			}
        	
        });
	}
	
	@Override
	public int getOrder() {
		return ApiGatewayConfig.REQUEST_LOGGING_FILTER;
	}



	
}
