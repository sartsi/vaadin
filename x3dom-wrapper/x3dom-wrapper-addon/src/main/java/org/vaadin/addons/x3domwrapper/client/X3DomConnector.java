package org.vaadin.addons.x3domwrapper.client;

import org.vaadin.addons.x3domwrapper.server.X3Dom;

import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(X3Dom.class)
public class X3DomConnector extends AbstractComponentConnector {
  @Override
  public X3DomState getState() {
    return (X3DomState) super.getState();
  }

  @Override
  public X3DomWidget getWidget() {
    return (X3DomWidget) super.getWidget();
  }

  public X3DomConnector() {
  }
}
