package cloud.cholewa.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
class EatonServiceInterfaceLights implements EatonService {
    @Override
    public Mono<ResponseEntity<String>> parse(String message) {
        log.info("Parsing message from [Eaton's lights] interface");
        return Mono.empty();
    }
}
