package org.vaadin.addons.filelazydownloader.client;

/**
 * 
 * {@link StreamFetchingState} holds state of stream fetching.
 *
 */
public enum StreamFetchingState {
  /*
   * Nothing happened, yet.
   */
  INITIAL,
  /*
   * Client first click.
   */
  INITIATED,
  /*
   * Server started to fetch/prepare stream.
   */
  PENDING,
  /*
   * Server successfully finished to fetch/prepare stream.
   */
  SUCCESSFUL,
  /*
   * Server failed to fetch/prepare stream.
   */
  FAILED,
}
