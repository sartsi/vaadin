package org.vaadin.addons.filelazydownloader.client;

public interface ContentSettings {
  /**
   * Gets {@link Content} for given {@link StreamFetchingState}
   * 
   * @param state
   *          {@link StreamFetchingState}
   * @return
   */
  Content getContent(StreamFetchingState state);
}
