package org.nowstart.admin.config;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final DiscoveryClient discoveryClient;

    @Bean
    @Primary
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {
        SwaggerUiConfigProperties properties = new SwaggerUiConfigProperties();

        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = discoveryClient.getServices().stream()
                .flatMap(serviceId -> discoveryClient.getInstances(serviceId).stream()
                        .map(instance -> {
                            String url = instance.getUri() + "/v3/api-docs";
                            log.info("Discovered service: {} with URL: {}", serviceId, url);
                            AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl =
                                    new AbstractSwaggerUiConfigProperties.SwaggerUrl();
                            swaggerUrl.setName(serviceId);
                            swaggerUrl.setUrl(url);
                            return swaggerUrl;
                        }))
                .collect(Collectors.toSet());

        properties.setUrls(urls);
        return properties;
    }
}
