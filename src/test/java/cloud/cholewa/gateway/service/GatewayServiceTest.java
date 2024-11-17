package cloud.cholewa.gateway.service;

import cloud.cholewa.eaton.infrastructure.error.EatonException;
import cloud.cholewa.gateway.device.client.DeviceConfigurationClient;
import cloud.cholewa.gateway.heating.client.HeatingClient;
import cloud.cholewa.gateway.infrastructure.error.ConfigurationCallException;
import cloud.cholewa.home.model.DeviceType;
import cloud.cholewa.home.model.EatonConfigurationResponse;
import cloud.cholewa.home.model.RoomName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GatewayServiceTest {

    @Mock
    private DeviceConfigurationClient deviceConfigurationClient;
    @Mock
    private HeatingClient heatingClient;
    @InjectMocks
    private GatewayService sut;

    @Test
    void should_throw_exception_when_eaton_message_is_not_valid() {
        sut.parseEatonMessage(null, "aa,bb")
            .as(StepVerifier::create)
            .verifyError(EatonException.class);

        verify(deviceConfigurationClient, never()).getDeviceConfiguration(any(), anyInt());
    }

    @Test
    void should_throw_exception_when_interfaceType_is_not_valid() {
        sut.parseEatonMessage("wrong", "5A, 11, A5")
            .as(StepVerifier::create)
            .verifyError(EatonException.class);

        verify(deviceConfigurationClient, never()).getDeviceConfiguration(any(), anyInt());
    }

    @Test
    void should_throw_exception_when_device_configuration_not_known() {
        when(deviceConfigurationClient.getDeviceConfiguration(any(), anyInt()))
            .thenThrow(ConfigurationCallException.class);

        sut.parseEatonMessage("blinds", "5A,C,C1,2C,62,3,0,1,58,0,0,43,5,A5")
            .as(StepVerifier::create)
            .verifyError(ConfigurationCallException.class);

        verify(deviceConfigurationClient, times(1)).getDeviceConfiguration(any(), anyInt());
    }

    @Test
    void should_return_empty_response_for_valid_message_and_device_configuration_available() {
        when(deviceConfigurationClient.getDeviceConfiguration(any(), anyInt()))
            .thenReturn(Mono.just(
                ResponseEntity.ok(EatonConfigurationResponse.builder()
                        .dataPoint(1)
                        .deviceType(DeviceType.BLINDS)
                        .roomName(RoomName.LIVING_ROOM)
                    .build())
            ));

        when(heatingClient.sendDeviceStatus(any())).thenReturn(Mono.empty());

        sut.parseEatonMessage("blinds", "5A,C,C1,2C,62,3,0,1,58,0,0,43,5,A5")
            .as(StepVerifier::create)
            .verifyComplete();

        verify(deviceConfigurationClient, times(1)).getDeviceConfiguration(any(), anyInt());
    }
}

