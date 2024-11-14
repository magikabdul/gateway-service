package cloud.cholewa.gateway.infrastructure.error;

import cloud.cholewa.commons.error.model.ErrorId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CustomError implements ErrorId {

    EATON_PARSING("Invalid Eaton message couldn't be parsed");

    private final String description;
}
