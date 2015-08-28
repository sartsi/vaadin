package org.vaadin.demo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageProvider {

  private static final Logger LOGGER = Logger
      .getLogger(ImageProvider.class.getName());

  public ImageProvider() {
  }

  @ResponseBody
  @RequestMapping(value = "/x3d/*")
  public byte[] handleRequest(final HttpServletRequest request,
      final HttpServletResponse response) {
    final String pathInfo = request.getPathInfo();
    try {
      if (pathInfo != null) {

        final HttpSession session = request.getSession();
        final ServletContext servletContext = session.getServletContext();
        final InputStream in = servletContext
            .getResourceAsStream(pathInfo.replace("/x3d/", "/demo/"));
        response.setStatus(200);
        return IOUtils.toByteArray(in);
      }
    } catch (final IOException e) {
      LOGGER.severe("IOException " + e.getMessage());
    }
    response.setStatus(404);
    return new byte[0];
  }
}
