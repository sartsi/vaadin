package org.vaadin.addons.nativeselectext.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.ListBoxExt;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.Field;
import com.vaadin.client.ui.VOptionGroupBase;

public class VNativeSelectExt extends VOptionGroupBase implements Field {

  private static final Logger LOGGER = Logger
      .getLogger(VNativeSelectExt.class.getName());
  public static final String CLASSNAME = "v-select-ext";

  protected ListBoxExt select;

  private boolean firstValueIsTemporaryNullItem = false;

  public VNativeSelectExt() {
    super(new ListBoxExt(false), CLASSNAME);
    select = getOptionsContainer();
    select.setVisibleItemCount(1);
    select.addChangeHandler(this);
    select.setStyleName(CLASSNAME + "-select");

    updateEnabledState();
  }

  protected ListBoxExt getOptionsContainer() {
    return (ListBoxExt) optionsContainer;
  }

  @Override
  public void buildOptions(final UIDL uidl) {
    select.clear();
    firstValueIsTemporaryNullItem = false;

    if (isNullSelectionAllowed() && !isNullSelectionItemAvailable()) {
      // can't unselect last item in singleselect mode
      select.addItem("", (String) null);
    }
    boolean selected = false;
    for (final Iterator<?> i = uidl.getChildIterator(); i.hasNext();) {
      final UIDL optionUidl = (UIDL) i.next();
      LOGGER.warning("fetching className");
      final String className = optionUidl.getStringAttribute("optionClassName");
      LOGGER.warning("className :" + className);
      if (className != null && !className.isEmpty()) {
        select.addItemExt(optionUidl.getStringAttribute("caption"),
            optionUidl.getStringAttribute("key"), className);
      } else {
        select.addItem(optionUidl.getStringAttribute("caption"),
            optionUidl.getStringAttribute("key"));
      }
      if (optionUidl.hasAttribute("selected")) {
        select.setItemSelected(select.getItemCount() - 1, true);
        selected = true;
      }
    }
    if (!selected && !isNullSelectionAllowed()) {
      // null-select not allowed, but value not selected yet; add null and
      // remove when something is selected
      select.insertItem("", (String) null, 0);
      select.setItemSelected(0, true);
      firstValueIsTemporaryNullItem = true;
    }
  }

  @Override
  protected String[] getSelectedItems() {
    final ArrayList<String> selectedItemKeys = new ArrayList<String>();
    for (int i = 0; i < select.getItemCount(); i++) {
      if (select.isItemSelected(i)) {
        selectedItemKeys.add(select.getValue(i));
      }
    }
    return selectedItemKeys.toArray(new String[selectedItemKeys.size()]);
  }

  @Override
  public void onChange(final ChangeEvent event) {

    if (select.isMultipleSelect()) {
      client.updateVariable(paintableId, "selected", getSelectedItems(),
          isImmediate());
    } else {
      client.updateVariable(paintableId, "selected",
          new String[] { "" + getSelectedItem() }, isImmediate());
    }
    if (firstValueIsTemporaryNullItem) {
      // remove temporary empty item
      select.removeItem(0);
      firstValueIsTemporaryNullItem = false;
    }
  }

  @Override
  public void setHeight(final String height) {
    select.setHeight(height);
    super.setHeight(height);
  }

  @Override
  public void setWidth(final String width) {
    select.setWidth(width);
    super.setWidth(width);
  }

  @Override
  public void setTabIndex(final int tabIndex) {
    getOptionsContainer().setTabIndex(tabIndex);
  }

  @Override
  protected void updateEnabledState() {
    select.setEnabled(isEnabled() && !isReadonly());
  }

  @Override
  public void focus() {
    select.setFocus(true);
  }
}
