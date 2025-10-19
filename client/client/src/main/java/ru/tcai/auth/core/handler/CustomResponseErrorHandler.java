package ru.tcai.auth.core.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import ru.tcai.auth.api.dto.response.ErrorResponse;
import ru.tcai.auth.core.exception.AuthClientException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class CustomResponseErrorHandler implements ResponseErrorHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) {
        throw new AuthClientException(parseErrorBody(response));
    }

    public static ErrorResponse parseErrorBody(ClientHttpResponse response) {
        try (InputStream inputStream = response.getBody()) {
            return OBJECT_MAPPER.readValue(inputStream, ErrorResponse.class);

        } catch (Exception e) {
            throw new AuthClientException("Auth service client body parsing error");
        }
    }
}
