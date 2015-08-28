package org.vaadin.addons.x3domwrapper.client;

public class X3DomUtils {
  public final static class Tags {
    public static final class X3D {
      public static final String NAME = "x3d";
      public static final String ATT_WIDTH = "width";
      public static final String ATT_HEIGHT = "height";

      public enum Attributes {
        WIDTH, HEIGHT,
      }
    }

    public static final class SCENE {
      public static final String NAME = "scene";
      public static final String ATT_WIDTH = "width";
      public static final String ATT_HEIGHT = "height";

      public enum Attributes {
        WIDTH, HEIGHT,
      }
    }

    public static final class INLINE {
      public static final String NAME = "inline";
      public static final String ATT_WIDTH = "width";
      public static final String ATT_HEIGHT = "height";

      public enum Attributes {
        URL
      }
    }
  }

  public static String toString(final int v) {
    return String.valueOf(v);
  }
}
