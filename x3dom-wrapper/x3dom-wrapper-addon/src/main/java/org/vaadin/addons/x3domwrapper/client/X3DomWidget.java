package org.vaadin.addons.x3domwrapper.client;

import java.util.logging.Logger;

import org.vaadin.addons.x3domwrapper.client.script.ScriptLoader;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;

public class X3DomWidget extends SimplePanel {
  private static final Logger LOGGER = Logger
      .getLogger(X3DomWidget.class.getName());

  public X3DomWidget() {
    super();
    ScriptLoader.tryToInjectScripts();
    final com.google.gwt.user.client.Element createDiv = DOM.createDiv();
    setElement(createDiv);
    setStyleName("v-x3dom-widget");
  }

  private String sceneName = "/demo/Deer.x3d";

  public void setSceneName(final String sceneName) {
    this.sceneName = sceneName;
    init();
  }

  private int sceneHeight = 400;
  private int sceneWidth = 400;

  public void init() {
    LOGGER.finest("initializing");
    getElement().removeAllChildren();
    final Element x3dNode = DOM.createElement(X3DomUtils.Tags.X3D.NAME);
    x3dNode.setAttribute(X3DomUtils.Tags.X3D.Attributes.HEIGHT.name(),
        X3DomUtils.toString(getSceneHeight()));
    x3dNode.setAttribute(X3DomUtils.Tags.X3D.Attributes.WIDTH.name(),
        X3DomUtils.toString(getSceneWidth()));
    DOM.appendChild(getElement(), x3dNode);
    final Element sceneNode = DOM.createElement(X3DomUtils.Tags.SCENE.NAME);
    DOM.appendChild(x3dNode, sceneNode);
    final Element inlineNode = DOM.createElement(X3DomUtils.Tags.INLINE.NAME);
    DOM.appendChild(sceneNode, inlineNode);
    inlineNode.setAttribute(X3DomUtils.Tags.INLINE.Attributes.URL.name(),
        sceneName);
  }

  public int getSceneHeight() {
    return sceneHeight;
  }

  public void setSceneHeight(final int height) {
    this.sceneHeight = height;
    init();
  }

  public int getSceneWidth() {
    return sceneWidth;
  }

  public void setSceneWidth(final int width) {
    this.sceneWidth = width;
    init();
  }

  public String getSceneName() {
    return sceneName;
  }
}