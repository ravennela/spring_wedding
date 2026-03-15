package com.example.online.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {

        Map<String, String> config = new HashMap<>();

        config.put("cloud_name", "dwtslw4zt");
        config.put("api_key", "825385462736727");
        config.put("api_secret", "hVf6qLazJ1SuAmVfKoQ3IbUESGI");

        return new Cloudinary(config);
    }
}