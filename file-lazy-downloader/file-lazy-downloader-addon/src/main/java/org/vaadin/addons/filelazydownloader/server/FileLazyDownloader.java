package org.vaadin.addons.filelazydownloader.server;

import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;

import org.vaadin.addons.filelazydownloader.client.ContentSettings;
import org.vaadin.addons.filelazydownloader.client.FileLazyDownloaderClientToServerRpc;
import org.vaadin.addons.filelazydownloader.client.FileLazyDownloaderState;
import org.vaadin.addons.filelazydownloader.client.StreamFetchingState;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

@SuppressWarnings("serial")
public class FileLazyDownloader extends FileDownloader {
  protected interface StateHolder {
    /**
     * Gets {@link StreamFetchingState}
     * 
     * @return {@link StreamFetchingState}
     */
    StreamFetchingState getCommunicationState();

    /**
     * Sets {@link StreamFetchingState}
     * 
     * @param communicationState
     */
    void setStreamFetchingState(StreamFetchingState communicationState);
  }

  private class StateHolderImpl implements StateHolder {
    private StreamFetchingState communicationState;

    @Override
    public synchronized StreamFetchingState getCommunicationState() {
      return communicationState;
    }

    @Override
    public synchronized void setStreamFetchingState(final StreamFetchingState communicationState) {
      this.communicationState = communicationState;
    }
  }

  private static final Logger LOGGER = Logger.getLogger(FileLazyDownloader.class.getName());

  public FileLazyDownloader(final Resource resource) {
    super(resource);
    init();
  }

  /**
   * Creates {@link FileLazyDownloader} with given {@link Resource} and
   * {@link ContentSettings}
   * 
   * @param resource
   * @param contentSettings
   */
  public FileLazyDownloader(final Resource resource, final ContentSettings contentSettings) {
    super(resource);
    Objects.requireNonNull(contentSettings);
    for (final StreamFetchingState state : StreamFetchingState.values()) {
      Objects.requireNonNull(contentSettings.getContent(state));
    }
    getState().successfulContent = contentSettings.getContent(StreamFetchingState.SUCCESSFUL).getContent();
    getState().successfulContentMode = contentSettings.getContent(StreamFetchingState.SUCCESSFUL).getContentMode();

    getState().pendingContent = contentSettings.getContent(StreamFetchingState.PENDING).getContent();
    getState().pendingContentMode = contentSettings.getContent(StreamFetchingState.PENDING).getContentMode();

    getState().failedContent = contentSettings.getContent(StreamFetchingState.FAILED).getContent();
    getState().failedContentMode = contentSettings.getContent(StreamFetchingState.FAILED).getContentMode();
    init();
  }

  @Override
  protected FileLazyDownloaderState getState() {
    return (FileLazyDownloaderState) super.getState();
  }

  @Override
  protected FileLazyDownloaderState getState(final boolean markAsDirty) {
    return (FileLazyDownloaderState) super.getState(markAsDirty);
  }

  private void init() {
    final StateHolder stateHolder = new StateHolderImpl();
    final FileLazyDownloaderClientToServerRpc fileLazyDownloaderClientToServerRpc = new FileLazyDownloaderClientToServerRpc() {

      @Override
      public synchronized void poll() {
        getState(true).communicationState = stateHolder.getCommunicationState();
      }

      @Override
      public void initiateStreamFetching() {
        LOGGER.fine("preparing.");
        final Resource fileDownloadResource = getFileDownloadResource();
        if (fileDownloadResource instanceof StreamResource) {
          final StreamResource streamResource = (StreamResource) fileDownloadResource;
          final Thread workerThread = new Thread(new Runnable() {

            @Override
            public void run() {
              final StreamSource streamSource = streamResource.getStreamSource();
              final InputStream stream = streamSource.getStream();
              if (stream != null) {
                streamResource.setStreamSource(new StreamSource() {

                  @Override
                  public InputStream getStream() {
                    return stream;
                  }
                });
                stateHolder.setStreamFetchingState(StreamFetchingState.SUCCESSFUL);
              } else {
                stateHolder.setStreamFetchingState(StreamFetchingState.FAILED);
              }
            }
          });
          workerThread.start();
          getState().communicationState = StreamFetchingState.PENDING;
        } else {
          stateHolder.setStreamFetchingState(StreamFetchingState.SUCCESSFUL);
        }
      }
    };
    registerRpc(fileLazyDownloaderClientToServerRpc, FileLazyDownloaderClientToServerRpc.class);
  }

  /**
   * Sets state if download shall be triggered instantly via java script.
   * default false.
   * 
   * @param value
   *          -
   */
  public void setInitiateInstantDownload(final boolean value) {
    getState(true).initiateInstantDownload = value;
  }
}
