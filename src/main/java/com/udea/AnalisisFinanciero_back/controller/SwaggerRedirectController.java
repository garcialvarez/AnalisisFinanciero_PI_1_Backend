package com.udea.AnalisisFinanciero_back.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class SwaggerRedirectController {
    @GetMapping("/api")
    public void redirectToSwagger(HttpServletResponse response) throws IOException {
        response.sendRedirect("/doc/swagger-ui.html");
    }
}
