package org.vaadin.addons.x3domwrapper.client.script;

import java.util.logging.Logger;

import org.vaadin.addons.x3domwrapper.client.X3DomWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;

public class ScriptLoader {

  private static final ScriptLoader INSTANCE = GWT.create(ScriptLoader.class);

  private static final Logger LOGGER = Logger
      .getLogger(X3DomWidget.class.getName());
  private static boolean isInjected;

  public static void tryToInjectScripts() {
    LOGGER.warning("starting tryToInjectScripts");
    if (!isInjected) {
      LOGGER.warning("tryToInjectScripts: not injkected, yet.");
      INSTANCE.injectResources();
      isInjected = true;
    }
  }

  protected void injectResources() {
    LOGGER.warning("started injectResources");
    inject(X3DomResources.INSTANCE.x3Dom().getText());
    inject(X3DomResources.INSTANCE.ammo().getText());
    inject(X3DomResources.INSTANCE.dashAll().getText());

    inject(X3DomResources.INSTANCE.BVHRefiner().getText());
    inject(X3DomResources.INSTANCE.CADGeometry().getText());
    inject(X3DomResources.INSTANCE.Geometry2D().getText());
    inject(X3DomResources.INSTANCE.Geometry3DExt().getText());
    inject(X3DomResources.INSTANCE.Geospatial().getText());
    inject(X3DomResources.INSTANCE.HAnim().getText());
    inject(X3DomResources.INSTANCE.Layout().getText());
    inject(X3DomResources.INSTANCE.RigidBodyPhysics().getText());
    inject(X3DomResources.INSTANCE.VolumeRendering().getText());
    LOGGER.warning("finished injectResources - js");
    // and also inject css
    LOGGER.warning("started injectResources - css");
    StyleInjector.inject(X3DomResources.INSTANCE.CSS().getText());
    LOGGER.warning("finished injectResources - css");
  }

  protected static native void inject(String script)
  /*-{
      $wnd.eval(script);
  }-*/;
}
