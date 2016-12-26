package org.vaadin.addons.closablebrowserwindowopener.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@Theme(Reindeer.THEME_NAME)
@Title("TestUi")
@SuppressWarnings("serial")
public class TestUi extends UI {

  @Override
  protected void init(final VaadinRequest request) {
    final VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.addComponent(new Label("this is just a test ui"));
    setContent(verticalLayout);
  }
}
