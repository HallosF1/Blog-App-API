package com.hallos.blogapp;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogappApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${application-description}") String description,
                                 @Value("${application-version}") String version) {

        return new OpenAPI()
                .info(new Info()
                        .title("Blog app API")
                        .version(version)
                        .description(description)
                        .license(new License().name("Hallos API License")));
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogappApplication.class, args);
    }

}
