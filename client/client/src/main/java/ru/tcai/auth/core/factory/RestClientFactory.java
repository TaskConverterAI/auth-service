package ru.tcai.auth.core.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.tcai.auth.core.handler.CustomResponseErrorHandler;

import java.net.http.HttpClient;
import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestClientFactory {

    public static <T> T createRestClient(
            String url,
            Duration connectTimeout,
            Duration readTimeout,
            Class<T> restClientClass
    ) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);

        RestClient restClient = RestClient.builder()
                .baseUrl(url)
                .defaultStatusHandler(new CustomResponseErrorHandler())
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(restClientClass);
    }
}
