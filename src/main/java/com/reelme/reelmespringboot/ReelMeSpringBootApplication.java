package com.reelme.reelmespringboot;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.HttpResource;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class ReelMeSpringBootApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(ReelMeSpringBootApplication.class, args);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://192.168.56.0/24", "http://192.168.1.0/24", "http://192.168.56.0/24:4200", "http://192.168.1.29:4200", "http://www.omdbapi.com", "https://api.themoviedb.org", "https://www.themoviedb.org", "https://image.tmdb.org"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
