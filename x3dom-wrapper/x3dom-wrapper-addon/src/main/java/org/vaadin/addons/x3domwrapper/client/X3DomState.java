package org.vaadin.addons.x3domwrapper.client;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.annotations.DelegateToWidget;

@SuppressWarnings("serial")
public class X3DomState extends AbstractComponentState {
  @DelegateToWidget
  public String sceneName;
  @DelegateToWidget
  public int sceneHeight;
  @DelegateToWidget
  public int sceneWidth;
}
