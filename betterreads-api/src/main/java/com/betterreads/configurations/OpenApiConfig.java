package com.betterreads.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * <p>
 * Configuration for OpenAPI Swagger 2.0 documentation
 * </p>
 */
@Configuration
public class OpenApiConfig {

    /**
     * <p>
     * Builds the Swagger Open API object
     * </p>
     */
    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("nick.kerker@gmail.com");
        contact.setName("thekerker");
        contact.setUrl("https://github.com/thekerker");

        License license = new License().name("GNU GENERAL PUBLIC LICENSE")
                .url("https://github.com/thekerker/BetterReads/blob/main/LICENSE");

        Info info = new Info()
                .title("Better Reads")
                .version("1.0")
                .contact(contact)
                .description("A better Goodreads")
                .license(license);

        return new OpenAPI().info(info);
    }
}
