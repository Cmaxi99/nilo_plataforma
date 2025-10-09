package com.niloeducation.platform.management.swagger.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Hidden;

import java.net.URI;

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)  // Agregar anotación de seguridad
public class SwaggerController {
    private final static URI SWAGGER_UI = UriBuilder.of("/swagger-ui").path("index.html").build();

    @Get
    @Hidden
    @Secured(SecurityRule.IS_ANONYMOUS)  // Agregar anotación de seguridad al método
    HttpResponse<?> home() {
        return HttpResponse.seeOther(SWAGGER_UI);
    }
}
