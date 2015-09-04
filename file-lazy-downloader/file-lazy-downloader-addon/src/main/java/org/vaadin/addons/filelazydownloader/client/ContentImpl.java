package org.vaadin.addons.filelazydownloader.client;

import java.util.Objects;

import com.vaadin.shared.ui.label.ContentMode;

public class ContentImpl implements Content {
  private final String content;
  private final ContentMode contentMode;

  public ContentImpl(final String content, final ContentMode contentMode) {
    this.content = Objects.requireNonNull(content);
    this.contentMode = Objects.requireNonNull(contentMode);
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public ContentMode getContentMode() {
    return contentMode;
  }
}
