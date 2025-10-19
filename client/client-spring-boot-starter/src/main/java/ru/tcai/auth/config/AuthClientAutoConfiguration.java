package ru.tcai.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tcai.auth.api.client.AuthRestClient;
import ru.tcai.auth.core.factory.RestClientFactory;

@Configuration
@ConditionalOnMissingBean(AuthRestClient.class)
@EnableConfigurationProperties(AuthClientProperties.class)
public class AuthClientAutoConfiguration {

    @ConditionalOnMissingBean(AuthRestClient.class)
    @Bean
    public AuthRestClient authRestClient(AuthClientProperties properties) {
        return RestClientFactory.createRestClient(
                properties.getBasepath(),
                properties.getConnectTimeout(),
                properties.getReadTimeout(),
                AuthRestClient.class
        );
    }
}
