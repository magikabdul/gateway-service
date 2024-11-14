package cloud.cholewa.gateway.model;

import cloud.cholewa.home.model.DeviceType;
import cloud.cholewa.home.model.EatonGateway;
import cloud.cholewa.home.model.RoomName;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EatonDeviceStatus {
    private Integer dataPoint;
    private String message;
    private RoomName roomName;
    private EatonGateway eatonGateway;
    private DeviceType deviceType;
    private Double temperature;
}
