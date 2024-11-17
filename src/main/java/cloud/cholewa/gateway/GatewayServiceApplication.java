package cloud.cholewa.gateway;

import cloud.cholewa.gateway.device.client.DeviceConfigurationClientConfig;
import cloud.cholewa.gateway.heating.client.HeatingClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    DeviceConfigurationClientConfig.class,
    HeatingClientConfig.class
})
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

}
