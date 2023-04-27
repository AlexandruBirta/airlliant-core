package ro.unibuc.fmi.airlliantcore.service;


import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ro.unibuc.fmi.airlliantcore.configuration.WebClientFilters;
import ro.unibuc.fmi.airlliantmodel.entity.Ticket;

import java.time.Duration;


@Service
public class AirlliantQuartzService {

    private final int retryMaxAttempts;
    private final int retryDurationInSeconds;
    private final int timeoutDurationInSeconds;
    private final String addTicketPath;
    private final String deleteTicketPath;
    private final WebClient webClient;

    @Autowired
    public AirlliantQuartzService(@Value("${airlliantquartz.retryMaxAttempts}") int retryMaxAttempts,
                                  @Value("${airlliantquartz.retryDurationInSeconds}") int retryDurationInSeconds,
                                  @Value("${airlliantquartz.timeoutDurationInSeconds}") int timeoutDurationInSeconds,
                                  @Value("${airlliantquartz.addTicketPath}") String addTicketPath,
                                  @Value("${airlliantquartz.deleteTicketPath}") String deleteTicketPath,
                                  @Value("${airlliantquartz.baseUrl}") String airlliantquartzBaseUrl,
                                  @Value("${airlliantquartz.username}") String airlliantquartzUsername,
                                  @Value("${airlliantquartz.password}") String airlliantquartzPassword,
                                  WebClient.Builder webClientBuilder,
                                  WebClientFilters webClientFilters) {

        this.retryMaxAttempts = retryMaxAttempts;
        this.retryDurationInSeconds = retryDurationInSeconds;
        this.timeoutDurationInSeconds = timeoutDurationInSeconds;
        this.addTicketPath = addTicketPath;
        this.deleteTicketPath = deleteTicketPath;
        this.webClient = webClientBuilder
                .baseUrl(airlliantquartzBaseUrl)
                .filter(ExchangeFilterFunctions.basicAuthentication(airlliantquartzUsername, airlliantquartzPassword))
                .filter(webClientFilters.logRequest())
                .filter(webClientFilters.logResponse())
                .filter(webClientFilters.passMdcContext())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

    }

    public void addTicket(Ticket ticket) {

        this.webClient.post()
                .uri(addTicketPath)
                .header("Origin-Application-Name", MDC.get("Application-Name"))
                .header("Correlation-Id", MDC.get("Correlation-Id"))
                .body(Mono.just(ticket), Ticket.class)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(timeoutDurationInSeconds))
                .retryWhen(
                        Retry.backoff(retryMaxAttempts, Duration.ofSeconds(retryDurationInSeconds))
                                .filter(WebClientFilters::is5xxServerError)
                )
                .block();

    }

    public void deleteTicket(Long ticketId) {

        this.webClient.delete()
                .uri(deleteTicketPath, ticketId)
                .header("Origin-Application-Name", MDC.get("Application-Name"))
                .header("Correlation-Id", MDC.get("Correlation-Id"))
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(timeoutDurationInSeconds))
                .retryWhen(
                        Retry.backoff(retryMaxAttempts, Duration.ofSeconds(retryDurationInSeconds))
                                .filter(WebClientFilters::is5xxServerError)
                )
                .block();

    }

}