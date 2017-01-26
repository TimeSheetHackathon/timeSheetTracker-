package org.thoughtworks.app.timesheetTracker.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thoughtworks.app.timesheetTracker.controller.TimeSheetTrackerController;


@Configuration
public class TimeSheetMvcConfiguration extends WebMvcConfigurerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TimeSheetTrackerController.class);

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        logger.info("Registering static resource path");
        registry.addViewController("/index").setViewName("index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        super.configurePathMatch(configurer);
        configurer.setUseSuffixPatternMatch(false);
    }

}
