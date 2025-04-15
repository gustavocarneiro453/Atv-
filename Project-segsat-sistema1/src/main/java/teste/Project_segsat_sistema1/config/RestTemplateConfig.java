package teste.Project_segsat_sistema1.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração do RestTemplate para comunicação HTTP
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .requestFactory(() -> {
                    var factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
                    factory.setConnectTimeout(10000);
                    factory.setReadTimeout(30000);
                    return factory;
                })
                .build();
    }
} 