package io.crm.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Value("${app.cors.allowedOrigins}")
    private String[] allowedOrigins;

//    @Bean @Qualifier("MyEnum")
//    public EnumResourceService enumResourceService() {
//        return new EnumResourceServiceImpl();
//    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasenames(
                "classpath:messages",
                "classpath:ValidationMessages",
                "classpath:i18n/enum"
        );
        source.setCacheSeconds(0);
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // .allowedOrigins(allowedOrigins)
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }

    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //    // registry.addResourceHandler("swagger-ui.html")
    //     registry.addResourceHandler("/**")
    //             .addResourceLocations("classpath:/static/")
    //             .addResourceLocations("classpath:/META-INF/resources/");
    // }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
}
