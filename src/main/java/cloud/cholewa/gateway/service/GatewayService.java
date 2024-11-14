package cloud.cholewa.gateway.service;

import cloud.cholewa.eaton.infrastructure.error.EatonParsingException;
import cloud.cholewa.eaton.utilities.MessageUtilities;
import cloud.cholewa.eaton.utilities.MessageValidator;
import cloud.cholewa.eaton.utilities.RoomControllerParser;
import cloud.cholewa.gateway.device.client.DeviceConfigurationClient;
import cloud.cholewa.gateway.model.EatonDeviceStatus;
import cloud.cholewa.home.model.EatonGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class GatewayService {

    private final DeviceConfigurationClient deviceConfigurationClient;

    public Mono<ResponseEntity<Void>> parseEatonMessage(final String interfaceType, final String fullMessage) {

        return Mono.just(fullMessage)
            .filter(MessageValidator::isValidEatonMessage)
            .flatMap(message -> Mono.zip(
                Mono.just(MessageUtilities.extractMessage(message)),
                Mono.just(findEatonGateway(interfaceType))
            ))
            .flatMap(t -> Mono.zip(
                Mono.just(t.getT1()),
                deviceConfigurationClient.getDeviceConfiguration(
                    t.getT2(),
                    MessageUtilities.extractDataPoint(t.getT1())
                )
            ))
            .map(t -> EatonDeviceStatus.builder()
                .dataPoint(MessageUtilities.extractDataPoint(t.getT1()))
                .message(t.getT1())
                .roomName(Objects.requireNonNull(t.getT2().getBody()).getRoomName())
                .deviceType(Objects.requireNonNull(t.getT2().getBody()).getDeviceType())
                .temperature(RoomControllerParser.calculateRoomTemperature(t.getT1()))
                .build()
            )
            .doOnNext(eatonDeviceStatus ->
                log.info(
                    "For room: {}, found device: {} with id: [{}] and temperature value: {}",
                    eatonDeviceStatus.getRoomName().name(),
                    eatonDeviceStatus.getDeviceType().name(),
                    eatonDeviceStatus.getDataPoint(),
                    eatonDeviceStatus.getTemperature()
                )
            )
            .map(eatonConfiguration -> ResponseEntity.ok().<Void>build());
    }

    private EatonGateway findEatonGateway(final String interfaceType) {
        try {
            return EatonGateway.fromValue(interfaceType);
        } catch (final IllegalArgumentException e) {
            throw new EatonParsingException("Unknown Eaton Gateway: " + interfaceType);
        }
    }
}
