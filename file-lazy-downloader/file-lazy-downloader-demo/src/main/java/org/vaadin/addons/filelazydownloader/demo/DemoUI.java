package org.vaadin.addons.filelazydownloader.demo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.filelazydownloader.client.ContentImpl;
import org.vaadin.addons.filelazydownloader.client.ContentSettings;
import org.vaadin.addons.filelazydownloader.client.ContentSettingsBulder;
import org.vaadin.addons.filelazydownloader.client.StreamFetchingState;
import org.vaadin.addons.filelazydownloader.server.FileLazyDownloader;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@Theme("reindeer")
@Title("FileLazyDownloader Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {
  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.vaadin.addons.filelazydownloader.demo.FileLazyDownloaderDemoWidgetSet")
  public static class Servlet extends VaadinServlet {
  }

  public DemoUI() {
  }

  @Override
  protected void init(final VaadinRequest request) {
    final VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setWidth(100, Unit.PERCENTAGE);
    setContent(verticalLayout);
    downloadWithTextContent(verticalLayout);
    spacer(verticalLayout);
    downloadWithHtmlContent(verticalLayout);
    spacer(verticalLayout);
    downloadWithHtmlContentFailed(verticalLayout);
    spacer(verticalLayout);
    downloadWithHtmlContentInstantly(verticalLayout);
    spacer(verticalLayout);
    downloadWithHtmlContentInstantlyFailed(verticalLayout);
    spacer(verticalLayout);
  }

  private void spacer(final VerticalLayout verticalLayout) {
    final CssLayout cssLayout = new CssLayout();
    cssLayout.setHeight(50, Unit.PIXELS);
    verticalLayout.addComponent(cssLayout);
  }

  private void downloadWithTextContent(final VerticalLayout verticalLayout) {
    final StreamResource streamResource = new StreamResource(
        new StreamSource() {
          @Override
          public InputStream getStream() {
            return new ByteArrayInputStream(
                UUID.randomUUID().toString().getBytes());
          }
        }, "standard" + UUID.randomUUID().toString() + ".txt");
    final FileLazyDownloader fld = new FileLazyDownloader(
        streamResource);

    final VerticalLayout innerLayout = new VerticalLayout();
    final Label title = new Label("file lazy downloader text content");
    title.setStyleName(Reindeer.LABEL_H1);

    final Label fileDownloaderLink = new Label("CLICK ME");
    fld.extend(fileDownloaderLink);
    innerLayout.addComponent(title);
    innerLayout.addComponent(fileDownloaderLink);
    verticalLayout.addComponent(innerLayout);
  }

  private void downloadWithHtmlContent(final VerticalLayout verticalLayout) {
    {
      final VerticalLayout innerLayout = new VerticalLayout();
      final StreamResource streamResource = new StreamResource(
          new StreamSource() {
            @Override
            public InputStream getStream() {
              return new ByteArrayInputStream(
                  UUID.randomUUID().toString().getBytes());
            }
          }, "is_" + UUID.randomUUID().toString() + ".txt");
      final Label title = new Label("file lazy downloader - raw content");
      title.setStyleName(Reindeer.LABEL_H1);

      final ContentSettings contenctSettings = ContentSettingsBulder
          .newInstance()
          .withContent(StreamFetchingState.FAILED,
              new ContentImpl("<H1>FAILED</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.INITIAL,
              new ContentImpl("<H1>INITIAL</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.INITIATED,
              new ContentImpl("<H1>INITIATED</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.PENDING,
              new ContentImpl("<H1>PENDING</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.SUCCESSFUL,
              new ContentImpl("<H1>SUCCESSFUL</H1>", ContentMode.HTML))
          .create();
      final FileLazyDownloader fld = new FileLazyDownloader(
          streamResource, contenctSettings);

      final Label fileDownloaderLink = extracted("CLICK ME");
      fld.extend(fileDownloaderLink);
      innerLayout.addComponent(title);
      innerLayout.addComponent(fileDownloaderLink);
      verticalLayout.addComponent(innerLayout);
    }
  }

  private Label extracted(final String string) {
    final Label fileDownloaderLink = new Label("<H1>" + string + "<//H1>");
    fileDownloaderLink.setContentMode(ContentMode.HTML);
    return fileDownloaderLink;
  }

  private void downloadWithHtmlContentFailed(
      final VerticalLayout verticalLayout) {
    {
      final VerticalLayout innerLayout = new VerticalLayout();
      final StreamResource streamResource = new StreamResource(
          new StreamSource() {
            @Override
            public InputStream getStream() {
              return null;
            }
          }, "is_" + UUID.randomUUID().toString() + ".txt");
      final Label title = new Label(
          "file lazy downloader - raw content - input stream is null");
      title.setStyleName(Reindeer.LABEL_H1);

      final ContentSettings contenctSettings = ContentSettingsBulder
          .newInstance()
          .withContent(StreamFetchingState.FAILED,
              new ContentImpl("<H1>FAILED</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.INITIAL,
              new ContentImpl("<H1>INITIAL</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.INITIATED,
              new ContentImpl("<H1>INITIATED</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.PENDING,
              new ContentImpl("<H1>PENDING</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.SUCCESSFUL,
              new ContentImpl("<H1>SUCCESSFUL</H1>", ContentMode.HTML))
          .create();
      final FileLazyDownloader fld = new FileLazyDownloader(
          streamResource, contenctSettings);

      final Label fileDownloaderLink = extracted("CLICK ME");
      fld.extend(fileDownloaderLink);
      innerLayout.addComponent(title);
      innerLayout.addComponent(fileDownloaderLink);
      verticalLayout.addComponent(innerLayout);
    }
  }

  private void downloadWithHtmlContentInstantly(
      final VerticalLayout verticalLayout) {
    {
      final VerticalLayout innerLayout = new VerticalLayout();
      final StreamResource streamResource = new StreamResource(
          new StreamSource() {
            @Override
            public InputStream getStream() {
              return new ByteArrayInputStream(
                  UUID.randomUUID().toString().getBytes());
            }
          }, "is_" + UUID.randomUUID().toString() + ".txt");
      final Label title = new Label(
          "file lazy downloader - raw content - instant download ");
      title.setStyleName(Reindeer.LABEL_H1);

      final ContentSettings contenctSettings = ContentSettingsBulder
          .newInstance()
          .withContent(StreamFetchingState.FAILED,
              new ContentImpl("<H1>FAILED</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.INITIAL,
              new ContentImpl("<H1>INITIAL</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.INITIATED,
              new ContentImpl("<H1>INITIATED</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.PENDING,
              new ContentImpl("<H1>PENDING</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.SUCCESSFUL,
              new ContentImpl("<H1>SUCCESSFUL</H1>", ContentMode.HTML))
          .create();
      final FileLazyDownloader fld = new FileLazyDownloader(
          streamResource, contenctSettings);

      final Label fileDownloaderLink = extracted("CLICK ME");
      fld.extend(fileDownloaderLink);
      innerLayout.addComponent(title);
      innerLayout.addComponent(fileDownloaderLink);
      verticalLayout.addComponent(innerLayout);

      fld.setInitiateInstantDownload(true);
    }
  }

  private void downloadWithHtmlContentInstantlyFailed(
      final VerticalLayout verticalLayout) {
    {
      final VerticalLayout innerLayout = new VerticalLayout();
      final StreamResource streamResource = new StreamResource(
          new StreamSource() {
            @Override
            public InputStream getStream() {
              return null;
            }
          }, "is_" + UUID.randomUUID().toString() + ".txt");
      final Label title = new Label(
          "file lazy downloader - raw content - instant download - inputstream is null");
      title.setStyleName(Reindeer.LABEL_H1);

      final ContentSettings contenctSettings = ContentSettingsBulder
          .newInstance()
          .withContent(StreamFetchingState.FAILED,
              new ContentImpl("<H1>FAILED</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.INITIAL,
              new ContentImpl("<H1>INITIAL</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.INITIATED,
              new ContentImpl("<H1>INITIATED</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.PENDING,
              new ContentImpl("<H1>PENDING</H1>", ContentMode.HTML))
          .withContent(StreamFetchingState.SUCCESSFUL,
              new ContentImpl("<H1>SUCCESSFUL</H1>", ContentMode.HTML))
          .create();
      final FileLazyDownloader fld = new FileLazyDownloader(
          streamResource, contenctSettings);

      final Label fileDownloaderLink = extracted("CLICK ME");
      fld.extend(fileDownloaderLink);
      innerLayout.addComponent(title);
      innerLayout.addComponent(fileDownloaderLink);
      verticalLayout.addComponent(innerLayout);

      fld.setInitiateInstantDownload(true);
    }
  }
}
