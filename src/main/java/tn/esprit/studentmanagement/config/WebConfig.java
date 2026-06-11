package tn.esprit.studentmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
  private static final String[] ALLOWED_HEADERS = {"Content-Type", "Authorization", "X-Requested-With"};

  @Value("${app.cors.allowed-origins:}")
  private String allowedOrigins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    if (allowedOrigins == null || allowedOrigins.isBlank()) {
      return;
    }

    registry.addMapping("/**")
        .allowedOrigins(allowedOrigins.split(","))
        .allowedMethods(ALLOWED_METHODS)
        .allowedHeaders(ALLOWED_HEADERS)
        .maxAge(3600);
  }
}
