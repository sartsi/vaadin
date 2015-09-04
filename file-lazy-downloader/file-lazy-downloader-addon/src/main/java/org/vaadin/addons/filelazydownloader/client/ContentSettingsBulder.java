package org.vaadin.addons.filelazydownloader.client;

import java.util.HashMap;
import java.util.Map;

public class ContentSettingsBulder {

  private ContentSettingsBulder() {
  }

  Map<StreamFetchingState, Content> CONTENT_MAP = new HashMap<StreamFetchingState, Content>();

  public ContentSettingsBulder withContent(final StreamFetchingState state,
      final Content content) {
    CONTENT_MAP.put(state, content);
    return this;
  }

  public static ContentSettingsBulder newInstance() {
    return new ContentSettingsBulder();
  }

  public ContentSettings create() {
    return new ContentSettings() {
      final DefaultContentSettings defaultContentSettings = new DefaultContentSettings();

      @Override
      public Content getContent(final StreamFetchingState state) {
        final Content content = CONTENT_MAP.get(state);
        if (content != null) {
          return content;
        }
        return defaultContentSettings.getContent(state);
      }
    };
  }
}
