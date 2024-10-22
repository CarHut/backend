package imageservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI imageServiceSwaggerAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Image service Swagger API")
                        .version("1.0")
                        .description("Documentation of API endpoints for the application"));
    }

}
