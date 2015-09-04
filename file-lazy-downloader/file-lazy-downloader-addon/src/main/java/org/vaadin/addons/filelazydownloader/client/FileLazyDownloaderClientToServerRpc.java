package org.vaadin.addons.filelazydownloader.client;

import com.vaadin.shared.communication.ServerRpc;

public interface FileLazyDownloaderClientToServerRpc extends ServerRpc {
  /**
   * Initiates stream fetching.
   */
  void initiateStreamFetching();

  /**
   * Polls server.
   */
  void poll();
}
