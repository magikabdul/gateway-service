package cloud.cholewa.gateway.service;

import cloud.cholewa.eaton.utilities.EatonTools;
import cloud.cholewa.eaton.utilities.EatonValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
class EatonServiceInterfaceBlinds implements EatonService {
    @Override
    public Mono<ResponseEntity<String>> parse(final String message) {
        log.info("Parsing message from [Eaton's blinds] interface");

        return Mono.just(message)
                .filter(EatonValidator::isValidEatonMessage)
                .map(EatonTools::extractMessage)
                .doOnNext(s -> log.info("Parsed message: {}", s))
                .map(s ->  new ResponseEntity<>(s, HttpStatus.OK))
                .switchIfEmpty(Mono.empty());

    }
}
