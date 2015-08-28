package org.vaadin.demo.configuaration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@EnableSpringConfigured
@ComponentScan(basePackages = "org.vaadin.demo.controller")
public class DispatcherConfiguration {
}
