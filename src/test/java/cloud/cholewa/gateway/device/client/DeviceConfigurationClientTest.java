package cloud.cholewa.gateway.device.client;

import cloud.cholewa.home.model.DeviceType;
import cloud.cholewa.home.model.EatonConfigurationResponse;
import cloud.cholewa.home.model.EatonGateway;
import cloud.cholewa.home.model.RoomName;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DeviceConfigurationClientTest {

    private MockWebServer mockWebServer;
    private DeviceConfigurationClient deviceConfigurationClient;
    private DeviceConfigurationClientConfig config;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        mockWebServer = new MockWebServer();
        mockWebServer.start(3000);

        config = new DeviceConfigurationClientConfig("localhost", "3000");

        deviceConfigurationClient = new DeviceConfigurationClient(
            WebClient.create(),
            config
        );
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        mockWebServer.shutdown();
    }

    @Test
    void should_return_device_configuration() {
        final ResponseEntity<EatonConfigurationResponse> response = ResponseEntity.ok(
            EatonConfigurationResponse.builder()
                .dataPoint(56)
                .deviceType(DeviceType.TEMPERATURE_SENSOR)
                .roomName(RoomName.ENTRANCE)
                .build()
        );

        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .addHeader("Content-Type", "application/json")
            .setBody("{\"dataPoint\":56,\"deviceType\":\"TemperatureSensor\",\"roomName\":\"Entrance\"}")
        );

        deviceConfigurationClient.getDeviceConfiguration(EatonGateway.BLINDS, 56)
            .as(StepVerifier::create)
            .assertNext(reply -> assertThat(reply.getBody())
                .isEqualTo(response.getBody()))
            .verifyComplete();
    }
}

//{"errors":[{"message":"Error processing configuration call","details":"Device configuration not found"}]}
