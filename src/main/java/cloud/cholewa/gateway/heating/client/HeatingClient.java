package cloud.cholewa.gateway.heating.client;

import cloud.cholewa.commons.error.model.Errors;
import cloud.cholewa.gateway.infrastructure.error.HeatingCallException;
import cloud.cholewa.home.model.DeviceStatusUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeatingClient {

    private final WebClient webClient;
    private final HeatingClientConfig config;

    public Mono<ResponseEntity<Void>> sendDeviceStatus(final DeviceStatusUpdate deviceStatusUpdate) {
        log.info(
            "Sending device status update for room: {}, device: {}",
            deviceStatusUpdate.getRoomName().name(),
            deviceStatusUpdate.getDeviceType().name()
        );

        return webClient.post()
            .uri(uriBuilder -> config.getUriBuilder(uriBuilder)
                .path("home/heating/device/amx/status:update")
                .build()
            )
            .body(BodyInserters.fromValue(deviceStatusUpdate))
            .retrieve()
            .onStatus(
                HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(Errors.class)
                    .flatMap(o -> Mono.error(new HeatingCallException(o.getHttpStatus(), o.getErrors())))
            )
            .toBodilessEntity();
    }
}
