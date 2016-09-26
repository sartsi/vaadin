/**
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.vaadin.addons.closablebrowserwindowopener.client;

import com.google.gwt.core.client.GWT;

public class ClosableBrowserWindowOpenerScriptLoader {

  private static final ClosableBrowserWindowOpenerScriptLoader INSTANCE = GWT.create(ClosableBrowserWindowOpenerScriptLoader.class);

  private static boolean scriptsInjected;

  public static void ensureInjected() {
    if (!scriptsInjected) {
      INSTANCE.injectResources();
      scriptsInjected = true;
    }
  }

  private native static boolean isJQueryPresent()
  /*-{
      if($wnd.jQuery)
          return true;
      return false;
  }-*/;

  private static native void inject(String script)
  /*-{
      $wnd.eval(script);
  }-*/;

  protected void injectResources() {
    if (!isJQueryPresent()) {
      inject(JavaScriptResources.INSTANCE.jquery().getText());
    }
  }
}
