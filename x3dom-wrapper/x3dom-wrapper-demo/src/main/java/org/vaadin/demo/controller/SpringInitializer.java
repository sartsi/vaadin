package org.vaadin.demo.controller;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.vaadin.demo.configuaration.DispatcherConfiguration;

public class SpringInitializer
    extends AbstractAnnotationConfigDispatcherServletInitializer {
  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[0];
    // return new Class[] { MainConfiguration.class };
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[] { DispatcherConfiguration.class };
  }

  @Override
  protected String[] getServletMappings() {
    // return new String[] { "/aaa", };
    return new String[] { "/aaa", "/*", };
  }
}
