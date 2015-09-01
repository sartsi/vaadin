package org.vaadin.addons.nativeselectext.server;

import java.util.Collection;
import java.util.HashMap;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.AbstractSelect;

@SuppressWarnings("serial")
public class NativeSelectExt extends AbstractSelect {
  protected final HashMap<Object, String> itemClassNames = new HashMap<Object, String>();

  @Override
  protected void paintItem(final PaintTarget target, final Object itemId)
      throws PaintException {
    super.paintItem(target, itemId);
    final String className = getItemClassName(itemId);
    if (className != null && !className.isEmpty()) {
      target.addAttribute("optionClassName", className);
    }
  }

  public String getItemClassName(final Object itemId) {
    if (itemClassNames.containsKey(itemId)) {
      return itemClassNames.get(itemId);
    }
    return null;
  }

  public void setItemClassName(final Object itemId,
      final String itemClassName) {
    if (itemId != null) {
      if (itemClassName == null) {
        itemClassNames.remove(itemId);
      } else {
        itemClassNames.put(itemId, itemClassName);
      }
      markAsDirty();
    }
  }

  public Item addItem(final Object itemId, final String itemClassName)
      throws UnsupportedOperationException {
    final Item item = super.addItem(itemId);
    setItemClassName(itemId, itemClassName);
    return item;
  }

  public NativeSelectExt() {
    super();
  }

  public NativeSelectExt(final String caption, final Collection<?> options) {
    super(caption, options);
  }

  public NativeSelectExt(final String caption, final Container dataSource) {
    super(caption, dataSource);
  }

  public NativeSelectExt(final String caption) {
    super(caption);
  }

  /**
   * Sets the width of the component so that it can display approximately the
   * given number of letters.
   * <p>
   * Calling {@code setColumns(10);} is equivalent to calling
   * {@code setWidth("10em");}
   * </p>
   * 
   * @deprecated As of 7.0. "Columns" does not reflect the exact number of
   *             characters that will be displayed. It is better to use setWidth
   *             together with "em" to control the width of the field.
   * @param columns
   *          the number of columns to set.
   */
  @Deprecated
  public void setColumns(int columns) {
    if (columns < 0) {
      columns = 0;
    }
    if (this.columns != columns) {
      this.columns = columns;
      markAsDirty();
    }
  }

  /**
   * Gets the number of columns for the component.
   * 
   * @see #setColumns(int)
   * @deprecated As of 7.0. "Columns" does not reflect the exact number of
   *             characters that will be displayed. It is better to use setWidth
   *             together with "em" to control the width of the field.
   */
  @Deprecated
  public int getColumns() {
    return columns;
  }

  @Override
  public void paintContent(final PaintTarget target) throws PaintException {
    target.addAttribute("type", "native");
    // Adds the number of columns
    if (columns != 0) {
      target.addAttribute("cols", columns);
    }

    super.paintContent(target);
  }

  @Override
  public void setMultiSelect(final boolean multiSelect)
      throws UnsupportedOperationException {
    if (multiSelect == true) {
      throw new UnsupportedOperationException("Multiselect not supported");
    }
  }

  @Override
  public void setNewItemsAllowed(final boolean allowNewOptions)
      throws UnsupportedOperationException {
    if (allowNewOptions == true) {
      throw new UnsupportedOperationException("newItemsAllowed not supported");
    }
  }

  private int columns = 0;
}
