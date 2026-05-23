package com.Asterisk.journalApp.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig(){
        return new OpenAPI().info(
                new Info().title("Journal App APIs")
                        .description("By Siddarth")
        ).servers(Arrays.asList(new Server().url("http://localhost:8081/journal").description("Local")))
                .tags(Arrays.asList(
                        new Tag().name("Public APIs"),
                        new Tag().name("User APIs"),
                        new Tag().name("Journal APIs"),
                        new Tag().name("Admin APIs")
                ));
    }
}
