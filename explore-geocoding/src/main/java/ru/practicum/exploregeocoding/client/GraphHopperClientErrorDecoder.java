package ru.practicum.exploregeocoding.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import ru.practicum.exploregeocoding.exception.ClientException;

/**
 * Декодер ошибок для GraphHopperClient
 */
public class GraphHopperClientErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        Exception defaultException = defaultDecoder.decode(methodKey, response);

        if (responseStatus.is4xxClientError()) {
            return new ClientException(String.format("Client error: %s", responseStatus), defaultException);
        }
        if (responseStatus.is5xxServerError()) {
            return new ClientException(String.format("GraphHopper internal server error: %s", responseStatus),
                    defaultException);
        }
        return defaultException;
    }
}
