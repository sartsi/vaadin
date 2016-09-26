package org.vaadin.addons.closablebrowserwindowopener.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.closablebrowserwindowopener.ClosableBrowserWindowOpener;
import org.vaadin.addons.closablebrowserwindowopener.WindowClosedEventHandler;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
@Title("ClosableBrowserWindowOpenerUi")
@Theme(Reindeer.THEME_NAME)
public class ClosableBrowserWindowOpenerUi extends UI {

  @WebServlet(value = { "/closable-browser-window-opener/*", "/VAADIN/*" }, asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = ClosableBrowserWindowOpenerUi.class,
      widgetset = "org.vaadin.addons.closablebrowserwindowopener.demo.ClosableBrowserWindowOpenerDemoWidgetSet")
  public static class Servlet extends VaadinServlet {
  }

  @Override
  protected void init(final VaadinRequest request) {
    final VerticalLayout layout = new VerticalLayout();
    setContent(layout);

    final NativeButton b = new NativeButton("open new window");
    final ClosableBrowserWindowOpener o = new ClosableBrowserWindowOpener(TestUi.class);
    o.extend(b);
    layout.addComponent(b);
    o.setWindowClosedEventHandler(new WindowClosedEventHandler() {

      @Override
      public void handle() {
        Notification.show("close event handler was triggered from client side");
      }
    });
  }
}
