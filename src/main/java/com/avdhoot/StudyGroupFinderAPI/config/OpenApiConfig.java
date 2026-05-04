package com.avdhoot.StudyGroupFinderAPI.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Sync Study API")
                        .version("1.0")
                        .description("API for finding and managing academic and skill-based study groups.")
                );
    }
}
