package cloud.cholewa.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class GatewayService {

    private final EatonServiceInterfaceBlinds blinds;
    private final EatonServiceInterfaceLights lights;

    public Mono<ResponseEntity<String>> parseEatonMessage(final String interfaceType, final String message) {
        return switch (interfaceType) {
            case "blinds" -> blinds.parse(message);
            case "lights" -> lights.parse(message);
            default -> throw new IllegalStateException("Unexpected value: " + interfaceType);
        };
    }
}
