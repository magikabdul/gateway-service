package cloud.cholewa.gateway.device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class DeviceService {

    private final Map<Integer, String> knownDevices = Map.of(
        40, "Hot Water",
        41, "Circulation",
        42, "Hot Water Pump",
        43, "Heating Pump",
        44, "Fireplace",
        45, "Outside",
        46, "Loft",
        47, "Office",
        48, "Tobi",
        49, "Liwia"
//            50, "Bedroom",
//            51, "Warderobe",
//            52, "BathUp",
//            53, "Salon",
//            54, "Cinema",
//            55, "BathDown",
//            56, "Entrance",
//            57, "Garage"
    );

    public Mono<String> findDevice(String deviceNumber) {
        String[] number = deviceNumber.split(",");

        log.info("Found device numer: {}", knownDevices.get(Integer.parseInt(number[2], 16)));

        return Mono.justOrEmpty(knownDevices.get(Integer.parseInt(number[2], 16)))
            .doOnNext(s -> log.info("----------------------------------Message from device: {}", s))
            .switchIfEmpty(Mono.error(() -> {
                log.error("-------------------------------No device in database");
                return new IllegalArgumentException();

            }));
    }
}
