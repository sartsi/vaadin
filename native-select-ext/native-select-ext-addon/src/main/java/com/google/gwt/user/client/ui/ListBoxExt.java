
package com.google.gwt.user.client.ui;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.i18n.client.HasDirection.Direction;

public class ListBoxExt extends ListBox {

  private static final Logger LOGGER = Logger
      .getLogger(ListBoxExt.class.getName());
  private static final int INSERT_AT_END_EXT = -1;

  public ListBoxExt(final boolean value) {
    super(value);
  }

  public void addItemExt(final String item, final String value,
      final String className) {
    insertItemExt(item, value, className, INSERT_AT_END_EXT);
  }

  public void insertItemExt(final String item, final String value,
      final String className, final int index) {
    insertItemExt(item, null, value, className, index);
  }

  public void insertItemExt(final String item, final Direction dir,
      final String value, final String className, int index) {
    final SelectElement select = getElement().cast();
    final OptionElement option = Document.get().createOptionElement();
    LOGGER.warning("Adding class name '" + className + "'");
    option.addClassName(className);
    setOptionText(option, item, dir);
    option.setValue(value);

    final int itemCount = select.getLength();
    if (index < 0 || index > itemCount) {
      index = itemCount;
    }
    if (index == itemCount) {
      select.add(option, null);
    } else {
      final OptionElement before = select.getOptions().getItem(index);
      select.add(option, before);
    }
  }
}
