package org.vaadin.addons.filelazydownloader.client;

import java.util.logging.Logger;

import org.vaadin.addons.filelazydownloader.server.FileLazyDownloader;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Profiler;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.FileDownloaderConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.label.ContentMode;

@SuppressWarnings("serial")
@Connect(FileLazyDownloader.class)
public class FileLazyDownloaderConnector extends FileDownloaderConnector {
  private static final Logger LOGGER = Logger.getLogger(FileLazyDownloaderConnector.class.getName());
  private IFrameElement iframe;

  @Override
  public FileLazyDownloaderState getState() {
    return (FileLazyDownloaderState) super.getState();
  }

  @Override
  public void onStateChanged(final StateChangeEvent stateChangeEvent) {
    super.onStateChanged(stateChangeEvent);
    LOGGER.info("on stata changed event");
    handleOnStateChangeEvent();
  }

  private void replaceContent(final StreamFetchingState communicaationState) {
    ContentMode contentMode = ContentMode.TEXT;
    String content = "";
    switch (communicaationState) {
      case PENDING: {
        contentMode = getState().pendingContentMode;
        content = getState().pendingContent;
        break;
      }
      case FAILED: {
        contentMode = getState().failedContentMode;
        content = getState().failedContent;
        break;
      }
      case SUCCESSFUL: {
        contentMode = getState().successfulContentMode;
        content = getState().successfulContent;
        break;
      }
      case INITIAL:
      case INITIATED:
      default: {
        return;
      }
    }
    boolean sinkOnloads = false;
    switch (contentMode) {
      case PREFORMATTED: {
        final PreElement preElement = Document.get().createPreElement();
        preElement.setInnerText(content);
        // replace existing content
        final Node firstChild = getWidget().getElement().getFirstChild();
        getWidget().getElement().replaceChild(preElement, firstChild);
        break;
      }
      case TEXT: {
        final DivElement createDivElement = Document.get().createDivElement();
        createDivElement.setInnerText(content);
        final Node firstChild = getWidget().getElement().getFirstChild();
        getWidget().getElement().replaceChild(createDivElement, firstChild);
        break;
      }
      case HTML:
      case RAW: {
        sinkOnloads = true;
      }
      case XML: {
        final DivElement createDivElement = Document.get().createDivElement();
        createDivElement.setInnerHTML(content);
        final Node firstChild = getWidget().getElement().getFirstChild();
        getWidget().getElement().replaceChild(createDivElement, firstChild);
      }
        break;
      default: {
        final DivElement createDivElement = Document.get().createDivElement();
        createDivElement.setInnerText("");
        final Node firstChild = getWidget().getElement().getFirstChild();
        getWidget().getElement().replaceChild(createDivElement, firstChild);
      }
        break;
    }
    Profiler.leave("LabelConnector.onStateChanged update content");

    if (sinkOnloads) {
      Profiler.enter("LabelConnector.onStateChanged sinkOnloads");
      WidgetUtil.sinkOnloadForImages(getWidget().getElement());
      Profiler.leave("LabelConnector.onStateChanged sinkOnloads");
    }
  }

  private Widget getWidget() {
    return ((ComponentConnector) getParent()).getWidget();
  }

  private void handleOnStateChangeEvent() {
    final ComponentConnector parent = (ComponentConnector) getParent();
    if (parent != null) {
      replaceContent(getState().communicationState);
      if (getState().communicationState == StreamFetchingState.SUCCESSFUL) {
        final Widget downloadWidget = parent.getWidget();
        clickHandlerRegistration = downloadWidget.addDomHandler(this, ClickEvent.getType());

        if (getState().initiateInstantDownload == true) {
          click(downloadWidget.getElement());
        }

        return;
      }
    }
  }

  public static native void click(Element e) /*-{
                                             e.click();
                                             }-*/;

  Timer timer;

  @Override
  public void onClick(final ClickEvent event) {
    handleClick();
  }

  private void handleClick() {
    if (getState().communicationState == StreamFetchingState.INITIAL) {
      onFirstClick();
      return;
    }
    if (getState().communicationState != StreamFetchingState.INITIAL) {
      if (getState().communicationState == StreamFetchingState.PENDING) {
        return;
      }
      if (getState().communicationState == StreamFetchingState.SUCCESSFUL) {
        final String url = getResourceUrl("dl");
        if (url != null && !url.isEmpty()) {
          if (iframe != null) {
            // make sure it is not on dom tree already, might start
            // multiple downloads at once
            iframe.removeFromParent();
          }
          iframe = Document.get().createIFrameElement();

          final Style style = iframe.getStyle();
          style.setVisibility(Visibility.HIDDEN);
          style.setHeight(0, Unit.PX);
          style.setWidth(0, Unit.PX);

          iframe.setFrameBorder(0);
          iframe.setTabIndex(-1);
          iframe.setSrc(url);
          RootPanel.getBodyElement().appendChild(iframe);
        }
        return;
      }
    }
  }

  private void onFirstClick() {
    clickHandlerRegistration.removeHandler();
    clickHandlerRegistration = null;
    final FileLazyDownloaderClientToServerRpc rpcProxy = getRpcProxy(FileLazyDownloaderClientToServerRpc.class);
    rpcProxy.initiateStreamFetching();
    timer = new Timer() {
      @Override
      public void run() {
        rpcProxy.poll();
        if (getState().communicationState == StreamFetchingState.PENDING) {
          LOGGER.fine("still in state pending.");
          replaceContent(getState().communicationState);
          return;
        }
        if (getState().communicationState == StreamFetchingState.SUCCESSFUL) {
          LOGGER.info("succeeded!");
          replaceContent(getState().communicationState);
          timer.cancel();
          timer = null;
          return;
        }
        if (getState().communicationState == StreamFetchingState.FAILED) {
          LOGGER.info("failed!");
          replaceContent(getState().communicationState);
          timer.cancel();
          timer = null;
          return;
        }
      }
    };
    timer.scheduleRepeating(getState().pollingPeriodInMilliSeconds);
    getState().communicationState = StreamFetchingState.INITIATED;
    replaceContent(getState().communicationState);
  }

  public FileLazyDownloaderConnector() {
  }

  HandlerRegistration clickHandlerRegistration;

  @Override
  protected void extend(final ServerConnector target) {
    final Widget downloadWidget = ((ComponentConnector) target).getWidget();
    downloadWidget.addStyleName("file-lazy-downloader-extension");
    clickHandlerRegistration = downloadWidget.addDomHandler(this, ClickEvent.getType());
  }

}
