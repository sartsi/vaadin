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

package org.vaadin.addons.closablebrowserwindowopener;

import java.util.Collections;
import java.util.Set;

import org.vaadin.addons.closablebrowserwindowopener.client.ClosableBrowserWindowOpenerServerRpc;
import org.vaadin.addons.closablebrowserwindowopener.client.ClosableBrowserWindowOpenerState;

import com.vaadin.server.AbstractExtension;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ApplicationConstants;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.UI;

/**
 * Component extension that opens a browser popup window when the extended
 * component is clicked.
 *
 * @author Vaadin Ltd
 * @since 7.0.0
 */
@SuppressWarnings("serial")
public class ClosableBrowserWindowOpener extends AbstractExtension {

  private static class ClosableBrowserWindowOpenerUIProvider extends UIProvider {

    private final String path;
    private final Class<? extends UI> uiClass;

    public ClosableBrowserWindowOpenerUIProvider(final Class<? extends UI> uiClass, final String path) {
      this.path = ensureInitialSlash(path);
      this.uiClass = uiClass;
    }

    private static String ensureInitialSlash(final String path) {
      if (path == null) {
        return null;
      } else if (!path.startsWith("/")) {
        return '/' + path;
      } else {
        return path;
      }
    }

    @Override
    public Class<? extends UI> getUIClass(final UIClassSelectionEvent event) {
      final String requestPathInfo = event.getRequest().getPathInfo();
      if (path.equals(requestPathInfo)) {
        return uiClass;
      } else {
        return null;
      }
    }
  }

  public static final String POPUP = "popup/";

  private final ClosableBrowserWindowOpenerUIProvider uiProvider;
  private WindowClosedEventHandler handler;

  /**
   * Creates a window opener that will open windows containing the provided UI
   * class
   *
   * @param uiClass
   *          the UI class that should be opened when the extended component is
   *          clicked
   */
  public ClosableBrowserWindowOpener(final Class<? extends UI> uiClass) {
    this(uiClass, generateUIClassUrl(uiClass));

    final ClosableBrowserWindowOpenerServerRpc closabeBrowserWindowOpenerServerRpc = new ClosableBrowserWindowOpenerServerRpc() {

      @Override
      public void sendClosedEvent() {
        if (handler != null) {
          handler.handle();
        }
      }
    };
    registerRpc(closabeBrowserWindowOpenerServerRpc, ClosableBrowserWindowOpenerServerRpc.class);
  }

  /**
   * Creates a window opener that will open windows containing the provided UI
   * using the provided path
   *
   * @param uiClass
   *          the UI class that should be opened when the extended component is
   *          clicked
   * @param path
   *          the path that the UI should be bound to
   */
  public ClosableBrowserWindowOpener(final Class<? extends UI> uiClass, final String path) {
    // Create a Resource with a translated URL going to the VaadinService
    this(new ExternalResource(ApplicationConstants.APP_PROTOCOL_PREFIX + path), new ClosableBrowserWindowOpenerUIProvider(uiClass, path));
  }

  /**
   * Creates a window opener that will open window to the provided resource
   *
   * @param resource
   *          the resource to open in the window
   */
  public ClosableBrowserWindowOpener(final Resource resource) {
    this(resource, null);
  }

  /**
   * Creates a window opener that will open windows to the provided URL
   *
   * @param url
   *          the URL to open in the window
   */
  public ClosableBrowserWindowOpener(final String url) {
    this(new ExternalResource(url));
  }

  private ClosableBrowserWindowOpener(final Resource resource, final ClosableBrowserWindowOpenerUIProvider uiProvider) {
    this.uiProvider = uiProvider;
    setResource(ClosableBrowserWindowOpenerState.locationResource, resource);
  }

  private static String generateUIClassUrl(final Class<? extends UI> uiClass) {
    return POPUP + uiClass.getSimpleName();
  }

  @Override
  public void attach() {
    super.attach();
    if (uiProvider != null && !getSession().getUIProviders().contains(uiProvider)) {
      getSession().addUIProvider(uiProvider);
    }
  }

  @Override
  public void detach() {
    if (uiProvider != null) {
      getSession().removeUIProvider(uiProvider);
    }
    super.detach();
  }

  public void extend(final AbstractComponent target) {
    super.extend(target);
  }

  /**
   * Gets the window features.
   *
   * @see #setFeatures(String)
   * @return
   */
  public String getFeatures() {
    return getState(false).features;
  }

  /**
   * Gets the value of a parameter set using
   * {@link #setParameter(String, String)}. If there is no parameter with the
   * given name, <code>null</code> is returned.
   *
   * @param name
   *          the name of the parameter to get, not <code>null</code>
   * @return the value of the parameter, or <code>null</code> there is no
   *         parameter
   *
   * @see #setParameter(String, String)
   * @see #getParameter(String)
   */
  public String getParameter(final String name) {
    if (name == null) {
      throw new IllegalArgumentException("Null not allowed");
    }
    return getState(false).parameters.get(name);
  }

  /**
   * Gets the names of all parameters set using
   * {@link #setParameter(String, String)}.
   *
   * @return an unmodifiable set of parameter names
   *
   * @see #setParameter(String, String)
   * @see #getParameter(String)
   */
  public Set<String> getParameterNames() {
    return Collections.unmodifiableSet(getState().parameters.keySet());
  }

  /**
   * Returns the resource for this instance.
   *
   * @since 7.4
   *
   * @return resource to open browser window
   */
  public Resource getResource() {
    return getResource(ClosableBrowserWindowOpenerState.locationResource);
  }

  /**
   * Gets that URI fragment configured for opened windows.
   *
   * @return the URI fragment string, or <code>null</code> if no fragment is
   *         configured.
   *
   * @see #setUriFragment(String)
   */
  public String getUriFragment() {
    return getState(false).uriFragment;
  }

  /**
   * Returns the URL for this BrowserWindowOpener instance. Returns {@code null}
   * if this instance is not URL resource based (a non URL based resource has
   * been set for it).
   *
   * @since 7.4
   *
   * @return URL to open in the new browser window/tab when the extended
   *         component is clicked
   */
  public String getUrl() {
    final Resource resource = getResource();
    if (resource instanceof ExternalResource) {
      return ((ExternalResource) resource).getURL();
    }
    return null;
  }

  /**
   * Gets the target window name.
   *
   * @see #setWindowName(String)
   *
   * @return the window target string
   */
  public String getWindowName() {
    return getState(false).target;
  }

  /**
   * Removes a parameter that has been set using
   * {@link #setParameter(String, String)}. Removing a parameter that has not
   * been set has no effect.
   *
   * @param name
   *          the name of the parameter to remove, not <code>null</code>
   *
   * @see #setParameter(String, String)
   */
  public void removeParameter(final String name) {
    if (name == null) {
      throw new IllegalArgumentException("Null not allowed");
    }
    getState().parameters.remove(name);
  }

  // Avoid breaking url to multiple lines
  // @formatter:off
  /**
   * Sets the features for opening the window. See e.g.
   * {@link https://developer.mozilla.org/en-US/docs/DOM/window.open#Position_and_size_features}
   * for a description of the commonly supported features.
   *
   * @param features
   *          a string with window features, or <code>null</code> to use the
   *          default features.
   */
  // @formatter:on
  public void setFeatures(final String features) {
    getState().features = features;
  }

  /**
   * Sets a parameter that will be added to the query string of the opened URI.
   * If the window is opened to contain a Vaadin UI, the parameter will be
   * available using {@link VaadinRequest#getParameter(String)} e.g. using the
   * request instance passed to {@link UI#init(VaadinRequest)}.
   * <p>
   * Setting a parameter with the same name as a previously set parameter will
   * replace the previous value.
   *
   * @param name
   *          the name of the parameter to add, not <code>null</code>
   * @param value
   *          the value of the parameter to add, not <code>null</code>
   *
   * @see #removeParameter(String)
   * @see #getParameterNames()
   * @see #getParameter(String)
   */
  public void setParameter(final String name, final String value) {
    if (name == null || value == null) {
      throw new IllegalArgumentException("Null not allowed");
    }
    getState().parameters.put(name, value);
  }

  /**
   * Sets the provided {@code resource} for this instance. The {@code resource}
   * will be opened in a new browser window/tab when the extended component is
   * clicked.
   *
   * @since 7.4
   *
   * @param resource
   *          resource to open
   */
  public void setResource(final Resource resource) {
    setResource(ClosableBrowserWindowOpenerState.locationResource, resource);
  }

  /**
   * Sets a URI fragment that will be added to the URI opened in the window. If
   * the window is opened to contain a Vaadin UI, the fragment will be available
   * using {@link Page#getUriFragment()} on the Page instance of the new UI.
   * <p>
   * The default value is <code>null</code>.
   *
   * @param uriFragment
   *          the URI fragment string that should be included in the opened URI,
   *          or <code>null</code> to preserve the original fragment of the URI.
   */
  public void setUriFragment(final String uriFragment) {
    getState().uriFragment = uriFragment;
  }

  /**
   * Sets the provided URL {@code url} for this instance. The {@code url} will
   * be opened in a new browser window/tab when the extended component is
   * clicked.
   *
   * @since 7.4
   *
   * @param url
   *          URL to open
   */
  public void setUrl(final String url) {
    setResource(new ExternalResource(url));
  }

  public void setWindowClosedEventHandler(final WindowClosedEventHandler handler) {
    this.handler = handler;
  }

  /**
   * Sets the target window name that will be used. If a window has already been
   * opened with the same name, the contents of that window will be replaced
   * instead of opening a new window. If the name is <code>null</code> or
   * <code>"_blank"</code>, a new window will always be opened.
   *
   * @param windowName
   *          the target name for the window
   */
  public void setWindowName(final String windowName) {
    getState().target = windowName;
  }

  @Override
  protected ClosableBrowserWindowOpenerState getState() {
    return (ClosableBrowserWindowOpenerState) super.getState();
  }

  @Override
  protected ClosableBrowserWindowOpenerState getState(final boolean markAsDirty) {
    return (ClosableBrowserWindowOpenerState) super.getState(markAsDirty);
  }

}
