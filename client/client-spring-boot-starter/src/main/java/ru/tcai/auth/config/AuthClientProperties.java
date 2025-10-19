package ru.tcai.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.auth-client", ignoreUnknownFields = false)
@ConditionalOnProperty(name = {"connect-timeout", "read-timeout", "basepath"}, prefix = "app.auth-client")
public class AuthClientProperties {

    private Duration connectTimeout;

    private Duration readTimeout;

    private String basepath;

}
