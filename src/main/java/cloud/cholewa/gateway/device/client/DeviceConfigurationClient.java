package cloud.cholewa.gateway.device.client;

import cloud.cholewa.commons.error.model.Errors;
import cloud.cholewa.gateway.infrastructure.error.ConfigurationCallException;
import cloud.cholewa.home.model.EatonConfigurationResponse;
import cloud.cholewa.home.model.EatonGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceConfigurationClient {

    private final WebClient webClient;
    private final DeviceConfigurationClientConfig config;

    public Mono<ResponseEntity<EatonConfigurationResponse>> getDeviceConfiguration(
        final EatonGateway gateway,
        final int datapoint
    ) {
        log.info("Querying device configuration from gateway: {} for data point {}", gateway, datapoint);

        return webClient
            .get()
            .uri(uriBuilder -> config
                .getUriBuilder(uriBuilder)
                .path("/home/device/configuration/eaton")
                .queryParam("eatonGateway", gateway.getValue())
                .queryParam("dataPoint", datapoint)
                .build()
            )
            .retrieve()
            .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(Errors.class)
                .flatMap(o -> Mono.error(new ConfigurationCallException(o.getHttpStatus(), o.getErrors()))))
            .toEntity(EatonConfigurationResponse.class);
    }
}
