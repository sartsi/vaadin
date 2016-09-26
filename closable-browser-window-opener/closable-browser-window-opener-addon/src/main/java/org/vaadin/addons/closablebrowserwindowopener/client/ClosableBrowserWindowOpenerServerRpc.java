/**
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.vaadin.addons.closablebrowserwindowopener.client;

import com.vaadin.shared.communication.ServerRpc;

/**
 * Client to server RPC.
 */
public interface ClosableBrowserWindowOpenerServerRpc extends ServerRpc {

  /**
   * Sends data to server.
   *
   * @param componentConnectorId
   *          - Component connector id - which is client connector id. may be
   *          null.
   * @param mouseEventDetails
   *          MouseEventDetails never null.
   * @param mousedownevent
   *          closablewindowbrowseropenerEvents may be null.
   */
  public void sendClosedEvent();

}