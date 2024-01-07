package cloud.cholewa.gateway.api;

import cloud.cholewa.gateway.service.GatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/amx")
@Slf4j
@RequiredArgsConstructor
public class GatewayController {

    private final GatewayService gatewayService;

    @GetMapping
    Mono<ResponseEntity<String>> incomingGet(
            @RequestParam(name = "message") final String message,
            @RequestBody final String body
    ) {
        log.info("Incoming AMX message from module: {} and content: {}", message, body);
        return gatewayService.parseEatonMessage(message, body);
    }

    @PostMapping
    void incomingPost() {
        log.info("AMX message on endpoint POST");
    }
}
