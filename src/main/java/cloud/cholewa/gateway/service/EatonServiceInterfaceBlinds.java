package cloud.cholewa.gateway.service;

import cloud.cholewa.eaton.utilities.MessageUtilities;
import cloud.cholewa.eaton.utilities.MessageValidator;
import cloud.cholewa.gateway.device.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
class EatonServiceInterfaceBlinds implements EatonService {

    private final DeviceService deviceService;

    @Override
    public Mono<ResponseEntity<String>> parse(final String message) {
        log.info("------------------------------ Parsing message from [Eaton's blinds] interface");

        return Mono.just(message)
            .filter(MessageValidator::isValidEatonMessage)
            .map(MessageUtilities::extractMessage)
//                .filter(s -> deviceService.findDevice(s))
            .doOnNext(deviceService::findDevice)
            .map(s -> new ResponseEntity<>(s, HttpStatus.OK))
            .switchIfEmpty(Mono.empty());

    }
}
