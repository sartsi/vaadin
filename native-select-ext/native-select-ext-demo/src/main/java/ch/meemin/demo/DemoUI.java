package ch.meemin.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.nativeselectext.server.NativeSelectExt;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@Theme("demo")
@Title("NativeSelectExt Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.vaadin.addons.nativeselectext.demo.NativeSelectExtDemoWidgetSet")
  public static class Servlet extends VaadinServlet {
  }

  NativeSelectExt nativeSelectExt = new NativeSelectExt();

  @Override
  protected void init(final VaadinRequest request) {

    nativeSelectExt.addItem("red", "redItem");
    nativeSelectExt.addItem("green", "greenItem");
    nativeSelectExt.addItem("blue", "blueItem");
    final Label title = new Label("NativeSelectExt Demo");
    title.setStyleName(Reindeer.LABEL_H1);

    final VerticalLayout layout = new VerticalLayout(title,
        new Label("open native selet and see neat colors"), nativeSelectExt);
    layout.setWidth(100, Unit.PERCENTAGE);
    setContent(layout);
  }
}
