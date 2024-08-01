package cloud.cholewa.gateway.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

interface EatonService {

    Mono<ResponseEntity<String>> parse(String message);
}
