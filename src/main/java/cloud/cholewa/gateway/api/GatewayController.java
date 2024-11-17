package cloud.cholewa.gateway.api;

import cloud.cholewa.gateway.service.GatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/amx")
@Slf4j
@RequiredArgsConstructor
public class GatewayController {

    private final GatewayService gatewayService;

    @GetMapping
    Mono<ResponseEntity<Void>> incomingGet(
        @RequestParam(name = "message") final String interfaceType,
        @RequestBody final String message
    ) {
        log.info("Incoming AMX message from module: {} and content: {}", interfaceType, message);
        return gatewayService.parseEatonMessage(interfaceType, message);
    }
}
