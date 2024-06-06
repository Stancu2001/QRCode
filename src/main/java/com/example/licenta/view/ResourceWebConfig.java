package com.example.licenta.view;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceWebConfig implements WebMvcConfigurer {
    final Environment environment;
    public ResourceWebConfig(Environment environment) {
        this.environment = environment;
    }
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
            //System.out.println(System.getProperty("user.dir"));
        String location = "file:///"+System.getProperty("user.dir").replace('\\','/')+"/uploads/";
        System.out.println(location);
        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
    }
}
