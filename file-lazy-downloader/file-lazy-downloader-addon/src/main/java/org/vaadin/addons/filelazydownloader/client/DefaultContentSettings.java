package org.vaadin.addons.filelazydownloader.client;

import com.vaadin.shared.ui.label.ContentMode;

public class DefaultContentSettings implements ContentSettings {

  @Override
  public Content getContent(final StreamFetchingState state) {
    switch (state) {
    case INITIAL:
      return new ContentImpl(StreamFetchingState.INITIAL.name(),
          ContentMode.TEXT);
    case INITIATED:
      return new ContentImpl(StreamFetchingState.INITIATED.name(),
          ContentMode.TEXT);
    case PENDING:
      return new ContentImpl(StreamFetchingState.PENDING.name(),
          ContentMode.TEXT);
    case FAILED:
      return new ContentImpl(StreamFetchingState.FAILED.name(),
          ContentMode.TEXT);
    case SUCCESSFUL:
      return new ContentImpl(StreamFetchingState.SUCCESSFUL.name(),
          ContentMode.TEXT);
    default:
      throw new UnsupportedOperationException(
          "no default opertation supported!");
    }
  }
}
