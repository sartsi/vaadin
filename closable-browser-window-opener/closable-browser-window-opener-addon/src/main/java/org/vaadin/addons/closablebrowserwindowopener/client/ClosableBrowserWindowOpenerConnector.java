/*
 * Copyright 2000-2014 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.vaadin.addons.closablebrowserwindowopener.client;

import java.util.Map.Entry;
import java.util.logging.Logger;

import org.vaadin.addons.closablebrowserwindowopener.ClosableBrowserWindowOpener;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ClosableWindow;
import com.google.gwt.user.client.ClosableWindow.Navigator;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.util.SharedUtil;

/**
 * Client-side code for {@link BrowserWindowOpener}
 *
 * @author Vaadin Ltd
 * @since 7.0.0
 */
@SuppressWarnings("serial")
@Connect(ClosableBrowserWindowOpener.class)
public class ClosableBrowserWindowOpenerConnector extends AbstractExtensionConnector implements ClickHandler {

  private static final Logger LOGGER = Logger.getLogger(ClosableBrowserWindowOpenerConnector.class.getName());

  private final ClosableBrowserWindowOpenerServerRpc rpc;

  public ClosableBrowserWindowOpenerConnector() {
    ClosableBrowserWindowOpenerScriptLoader.ensureInjected();
    rpc = RpcProxy.create(ClosableBrowserWindowOpenerServerRpc.class, this);
  }

  @Override
  public ClosableBrowserWindowOpenerState getState() {
    return (ClosableBrowserWindowOpenerState) super.getState();
  }

  @Override
  public void onClick(final ClickEvent event) {
    String url = getResourceUrl(ClosableBrowserWindowOpenerState.locationResource);
    url = addParametersAndFragment(url);
    if (url != null) {
      final JavaScriptObject window = ClosableWindow.openWithReference(url, getState().target, getState().features);
      final String userAgent = Navigator.getUserAgent();
      if (userAgent != null) {
        LOGGER.fine("user agent is : ''" + userAgent);
        final String ua = userAgent.toLowerCase();
        LOGGER.info("user agent: " + ua);
        if (ua.contains("msie") || ua.contains("trident") || ua.contains("edge/")) {
          if (ua.contains("msie 7.0")) {
            LOGGER.info("registering for ie 7.0!");
            registerOnBeforeUnloadHandlerForIe7(window);
          } else if (ua.contains("edge/")) {
            LOGGER.info("registering for microsoft edge!");
            registerOnBeforeUnloadHandlerForEdge(window);
          } else if (ua.contains("msie 9.0")) {
          } else if (ua.contains("msie 8.0")) {
            LOGGER.info("registering for ie 8.0!");
            registerOnBeforeUnloadHandlerForIe7(window);
          } else if (ua.contains("msie 9.0")) {
            LOGGER.info("registering for ie 9.0!");
            registerOnBeforeUnloadHandlerForIe7(window);
          } else {
            LOGGER.info("registering for all ie <> 8.0 or 9.0!");
            registerOnBeforeUnloadHandlerForIe(window);
          }
        } else {
          LOGGER.info("registering for ff / chrome / opera / ms ie 8.0 browsers!");
          registerOnBeforeUnloadHandler(window);
        }
      } else {
        LOGGER.info("user agent is null!");
      }
    }
  }

  public void sendClosedEvent() {
    rpc.sendClosedEvent();
  }

  @Override
  protected void extend(final ServerConnector target) {
    final Widget targetWidget = ((ComponentConnector) target).getWidget();
    targetWidget.addDomHandler(this, ClickEvent.getType());
  }

  native JavaScriptObject registerOnBeforeUnloadHandler(JavaScriptObject window) /*-{
                                                                                 var callee = this;
                                                                                 var oldOnbeforeunload = window.onbeforeunload;
                                                                                 function propagateClosedEvent(event) {
                                                                                 callee.@org.vaadin.addons.closablebrowserwindowopener.client.ClosableBrowserWindowOpenerConnector::sendClosedEvent()();
                                                                                 if(typeof oldOnbeforeunload !== 'undefined' && oldOnbeforeunload != null) {
                                                                                 oldOnbeforeunload(event);
                                                                                 }
                                                                                 }
                                                                                 window.onbeforeunload = propagateClosedEvent;
                                                                                 }-*/;

  native JavaScriptObject registerOnBeforeUnloadHandlerForEdge(JavaScriptObject givenWindow) /*-{
                                                                                              givenWindow.focus();
                                                                                              var callee = this;
                                                                                              window.console && console.log('registering unload handler');
                                                                                              var onunloadFunction = function() {
                                                                                               window.console && console.log('window unloading');
                                                                                               callee.@org.vaadin.addons.closablebrowserwindowopener.client.ClosableBrowserWindowOpenerConnector::sendClosedEvent()();
                                                                                              };
                                                                                              givenWindow.addEventListener("unload", onunloadFunction, true);
                                                                                              window.console && console.log('finished registering unload handler');
                                                                                              }-*/;

  native JavaScriptObject registerOnBeforeUnloadHandlerForEdgeLastWorking(JavaScriptObject givenWindow) /*-{
                                                                                                        givenWindow.focus();
                                                                                                        alert("registering");
                                                                                                        var callee = this;
                                                                                                        window.console && console.log('registering beforeunload and unload handlers');
                                                                                                        var onbeforeunloadFunction = function() {
                                                                                                        alert("calling server for event propagation");
                                                                                                        window.console && console.log('window before unloading');
                                                                                                        callee.@org.vaadin.addons.closablebrowserwindowopener.client.ClosableBrowserWindowOpenerConnector::sendClosedEvent()();
                                                                                                        };
                                                                                                        givenWindow.onbeforeunload = onbeforeunloadFunction;
                                                                                                        var onunloadFunction = function() {
                                                                                                        callee.@org.vaadin.addons.closablebrowserwindowopener.client.ClosableBrowserWindowOpenerConnector::sendClosedEvent()();
                                                                                                        window.console && console.log('window unloading');
                                                                                                        };
                                                                                                        givenWindow.onunload = onunloadFunction;
                                                                                                        window.console && console.log('finished registering beforeunload and unload handlers');
                                                                                                        }-*/;

  native JavaScriptObject registerOnBeforeUnloadHandlerForIe(JavaScriptObject givenWindow) /*-{
                                                                                           var callee = this;
                                                                                           var oldonunload = givenWindow.onunload;
                                                                                           function callClosedEvent() {
                                                                                           callee.@org.vaadin.addons.closablebrowserwindowopener.client.ClosableBrowserWindowOpenerConnector::sendClosedEvent()();
                                                                                           if(typeof oldonunload !== 'undefined' && oldonunload != null) {
                                                                                           oldonunload();
                                                                                           }
                                                                                           }
                                                                                           $wnd.jQuery(givenWindow).unload(function() {
                                                                                           callClosedEvent();
                                                                                           });
                                                                                           }-*/;

  native JavaScriptObject registerOnBeforeUnloadHandlerForIe7(JavaScriptObject givenWindow) /*-{
                                                                                            var callee = this;
                                                                                            var jq = $wnd.jQuery;
                                                                                            window.console && console.log('registering befereunload and unload handlers');
                                                                                            jq(givenWindow).bind("beforeunload",function(event) {
                                                                                            window.console && console.log('window before unloading');
                                                                                            callee.@org.vaadin.addons.closablebrowserwindowopener.client.ClosableBrowserWindowOpenerConnector::sendClosedEvent()();
                                                                                            });
                                                                                            jq(givenWindow).bind("unload",function() {
                                                                                              window.console && console.log('window unloading');
                                                                                            });
                                                                                            window.console && console.log('finished registering beforeunload and unload handlers');
                                                                                            }-*/;

  private String addParametersAndFragment(String url) {
    if (url == null) {
      return null;
    }

    if (!getState().parameters.isEmpty()) {
      final StringBuilder params = new StringBuilder();
      for (final Entry<String, String> entry : getState().parameters.entrySet()) {
        if (params.length() != 0) {
          params.append('&');
        }
        params.append(URL.encodeQueryString(entry.getKey()));
        params.append('=');

        final String value = entry.getValue();
        if (value != null) {
          params.append(URL.encodeQueryString(value));
        }
      }

      url = SharedUtil.addGetParameters(url, params.toString());
    }

    if (getState().uriFragment != null) {
      // Replace previous fragment or just add to the end of the url
      url = url.replaceFirst("#.*|$", "#" + getState().uriFragment);
    }

    return url;
  }
}
