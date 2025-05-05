package com.technokratos.vcs2.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "VCS2 api",
                description = "Возможности API для VCS2",
                version = "1.0.0",
                contact = @Contact(
                        name = "Vladislav Danilov",
                        email = "helloampro@gmail.com"
                )
        )
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic"
)
public class OpenApiConfig {
}