package com.adrian.bookstoreapi;

import com.adrian.bookstoreapi.auth.jwt.JwtProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class BookstoreapiApplication {

    @Bean
    public ModelMapper modelMapper() {
        // register as a @Bean to inject
        return new ModelMapper();
    }


    public static void main(String[] args) {
        SpringApplication.run(BookstoreapiApplication.class, args);
    }

}
