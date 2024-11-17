package cloud.cholewa.gateway.heating.client;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriBuilder;

@ConfigurationProperties("internal.service.heating")
public record HeatingClientConfig(
    @NotNull String host,
    @NotNull String port
) {
    public UriBuilder getUriBuilder(final UriBuilder uriBuilder) {
        return uriBuilder.scheme("http").host(host).port(port);
    }
}
