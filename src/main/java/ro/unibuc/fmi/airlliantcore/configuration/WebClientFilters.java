package ro.unibuc.fmi.airlliantcore.configuration;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;


@Slf4j
@Component
@AllArgsConstructor
public class WebClientFilters {

    public static final String GENERIC_PGW_ERROR_CODE = "500";
    public static final String GENERIC_PGW_ERROR_MESSAGE = "PGW encountered an unexpected error.";

    public static boolean is5xxServerError(Throwable throwable) {
        return throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
    }

    public ExchangeFilterFunction logRequest() {

        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Request: %s %s --- HEADERS ", clientRequest.method(), clientRequest.url()));
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> sb.append("[").append(name).append("=").append(value).append("] ")));
            log.debug(sb.toString());

            return Mono.just(clientRequest);

        });

    }

    public ExchangeFilterFunction logResponse() {

        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Response: %s --- HEADERS ", clientResponse.statusCode()));
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> sb.append("[").append(name).append("=").append(value).append("] ")));
            log.debug(sb.toString());

            return Mono.just(clientResponse);

        });

    }

    public ExchangeFilterFunction passMdcContext() {

        return (request, next) -> {

            // here runs on main (request's) thread
            Map<String, String> map = MDC.getCopyOfContextMap();

            return next.exchange(request).doOnNext(value -> {

                // here runs on reactor's thread
                if (map != null) {
                    MDC.setContextMap(map);
                }

            });

        };

    }

}