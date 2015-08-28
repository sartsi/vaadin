package org.vaadin.addons.x3domwrapper.server;

import org.vaadin.addons.x3domwrapper.client.X3DomClientToServerRpc;
import org.vaadin.addons.x3domwrapper.client.X3DomState;

import com.vaadin.ui.AbstractComponent;

@SuppressWarnings("serial")
public class X3Dom extends AbstractComponent {

  public X3Dom() {
    init();
  }

  @Override
  protected X3DomState getState() {
    return (X3DomState) super.getState();
  }

  @Override
  protected X3DomState getState(final boolean markAsDirty) {
    return (X3DomState) super.getState(markAsDirty);
  }

  private void init() {
    final X3DomClientToServerRpc clientToServerRpc = new X3DomClientToServerRpc() {
      @Override
      public void futureMethod() {
      }
    };
    registerRpc(clientToServerRpc, X3DomClientToServerRpc.class);
  }

  public void setSceneHeight(final int height) {
    getState(true).sceneHeight = height;
  }

  public void setSceneWidth(final int width) {
    getState(true).sceneWidth = width;
  }

  public void setSceneName(final String sceneName) {
    getState(true).sceneName = sceneName;
  }
}
