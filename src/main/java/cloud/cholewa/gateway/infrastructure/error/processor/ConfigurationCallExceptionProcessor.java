package cloud.cholewa.gateway.infrastructure.error.processor;

import cloud.cholewa.commons.error.model.ErrorMessage;
import cloud.cholewa.commons.error.model.Errors;
import cloud.cholewa.commons.error.processor.ExceptionProcessor;
import cloud.cholewa.gateway.infrastructure.error.ConfigurationCallException;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class ConfigurationCallExceptionProcessor implements ExceptionProcessor {

    @Override
    public Errors apply(final Throwable throwable) {
        ConfigurationCallException exception = (ConfigurationCallException) throwable;

        return Errors.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .errors(Collections.singleton(
                    ErrorMessage.builder()
                        .message("Error processing configuration call")
                        .details(exception.getErrorMessages().stream().findFirst().orElseThrow().getMessage())
                        .build()
                )
            )
            .build();
    }
}
