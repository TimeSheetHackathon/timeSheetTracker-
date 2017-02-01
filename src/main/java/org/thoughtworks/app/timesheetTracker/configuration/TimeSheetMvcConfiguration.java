package org.thoughtworks.app.timesheetTracker.configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thoughtworks.app.timesheetTracker.controller.TimeSheetTrackerController;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.thoughtworks.app.timesheetTracker.controller")
public class TimeSheetMvcConfiguration extends WebMvcConfigurerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TimeSheetTrackerController.class);

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");
        registry.viewResolver(viewResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations(
                "/static/");
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/WEB-INF/views/");
        bean.setSuffix(".html");
        return bean;
    }

    @Bean
    public AmazonS3Client amazonS3Client() {
        return new AmazonS3Client(new DefaultAWSCredentialsProviderChain());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
