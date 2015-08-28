package org.vaadin.addons.x3domwrapper.client.script;

/*
 * #%L
 * Vaadin Charts
 * %%
 * Copyright (C) 2014 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Add-On License 3.0
 * (CVALv3).
 * 
 * See the file licensing.txt distributed with this software for more
 * information about licensing.
 * 
 * You should have received a copy of the CVALv3 along with this program.
 * If not, see <https://vaadin.com/license/cval-3>.
 * #L%
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface X3DomResources extends ClientBundle {
  public static final X3DomResources INSTANCE = GWT
      .create(X3DomResources.class);

  @Source("x3dom.js")
  TextResource x3Dom();

  @Source("ammo.js")
  TextResource ammo();

  @Source("dash.all.js")
  TextResource dashAll();

  @Source("components/BVHRefiner.js")
  TextResource BVHRefiner();

  @Source("components/CADGeometry.js")
  TextResource CADGeometry();

  @Source("components/Geometry2D.js")
  TextResource Geometry2D();

  @Source("components/Geometry3DExt.js")
  TextResource Geometry3DExt();

  @Source("components/Geospatial.js")
  TextResource Geospatial();

  @Source("components/H-Anim.js")
  TextResource HAnim();

  @Source("components/Layout.js")
  TextResource Layout();

  @Source("components/RigidBodyPhysics.js")
  TextResource RigidBodyPhysics();

  @Source("components/VolumeRendering.js")
  TextResource VolumeRendering();

  @Source("x3dom.css")
  TextResource CSS();
}
