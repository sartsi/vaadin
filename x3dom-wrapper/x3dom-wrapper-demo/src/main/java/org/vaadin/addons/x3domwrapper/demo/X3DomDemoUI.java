package org.vaadin.addons.x3domwrapper.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.x3domwrapper.server.X3Dom;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@Theme("reindeer")
@Title("X3Dom Addon Demo")
@SuppressWarnings("serial")
public class X3DomDemoUI extends UI {
  @WebServlet(value = { "/x3dom/*", "/VAADIN/*" }, asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = X3DomDemoUI.class, widgetset = "org.vaadin.addons.x3domwrapper.demo.X3DomWrapperDemoWidgetSet")
  public static class Servlet extends VaadinServlet {
  }

  public X3DomDemoUI() {
  }

  @Override
  protected void init(final VaadinRequest request) {
    final VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setWidth(100, Unit.PERCENTAGE);
    setContent(verticalLayout);
    final X3Dom x3Dom = new X3Dom();
    x3Dom.setSizeFull();
    x3Dom.setSceneHeight(500);
    x3Dom.setSceneWidth(500);
    x3Dom.setSceneName("./x3d/FormulaOneRaceCar.x3d");
    extracted(verticalLayout, x3Dom);
  }

  private void extracted(final VerticalLayout verticalLayout,
      final X3Dom x3Dom) {
    final VerticalLayout innerLayout = new VerticalLayout();
    final Label title = new Label();
    title.setContentMode(ContentMode.HTML);
    title.setValue(
        "<div>x3dom wrapper, <a target=\"_blank\" href=\"http://www.x3dom.org/\">http://www.x3dom.org/</a></div>");
    title.setStyleName(Reindeer.LABEL_H1);
    x3Dom.addStyleName("x3dom-demo");
    innerLayout.addComponent(title);
    innerLayout.addComponent(x3Dom);
    verticalLayout.addComponent(innerLayout);
  }
}
