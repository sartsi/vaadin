package org.vaadin.addons.filelazydownloader.client;

import com.vaadin.shared.communication.SharedState;
import com.vaadin.shared.ui.label.ContentMode;

@SuppressWarnings("serial")
public class FileLazyDownloaderState extends SharedState {
  public StreamFetchingState communicationState = StreamFetchingState.INITIAL;
  public int pollingPeriodInMilliSeconds = 2000;
  public ContentMode pendingContentMode = ContentMode.TEXT;
  public ContentMode failedContentMode = ContentMode.TEXT;
  public ContentMode successfulContentMode = ContentMode.TEXT;
  public String failedContent = StreamFetchingState.FAILED.name();
  public String pendingContent = StreamFetchingState.PENDING.name();
  public String successfulContent = StreamFetchingState.SUCCESSFUL.name();
  public boolean initiateInstantDownload = false;
}
