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
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface JavaScriptResources extends ClientBundle {
  public static final JavaScriptResources INSTANCE = GWT.create(JavaScriptResources.class);

  @Source("jquery.min.js")
  TextResource jquery();
}
