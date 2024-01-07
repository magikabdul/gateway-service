package cloud.cholewa.gateway.service;

import reactor.core.publisher.Mono;

interface EatonService {

    Mono<Void> parse(String message);
}
