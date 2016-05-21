/*******************************************************************************
 * Copyright (c) 2006-2007 Nicolas Richeton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors :
 *    Nicolas Richeton (nicolas.richeton@gmail.com) - initial implementation
 *******************************************************************************/
package com.lehanh.pama.ui.sample;

import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This widget displays a simple gallery with some content.<br/>
 * Scrolling is vertical.<br/>
 * <br/>
 * 
 * <p>
 * NOTE: THIS WIDGET AND ITS API ARE STILL UNDER DEVELOPMENT. THIS IS A
 * PRE-RELEASE ALPHA VERSION. USERS SHOULD EXPECT API CHANGES IN FUTURE
 * VERSIONS.
 * </p>
 * 
 * @author Nicolas Richeton (nicolas.richeton@gmail.com)
 */

public class SnippetSimple {

	public static void main(String[] args) {
		Display display = new Display();
		Image itemImage = new Image(display, "eclipse_rcp_init_install_detail.png");
		// C:\\Users\\vinhhnguyen\\Desktop\\eclipse_rcp_init_install_detail.png
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		createLabel(itemImage, shell);	
		//createGallery(itemImage, shell);
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		if (itemImage != null)
			itemImage.dispose();
		display.dispose();
	}

	private static void createLabel(Image itemImage, Shell shell) {//		Composite parent = new
        CLabel label = new CLabel(shell, SWT.BORDER);
        label.setImage(itemImage);
        label.setText("This is a CLabel !!");	
	}

	private static void createGallery(Image itemImage, Composite shell) {
		Gallery gallery = new Gallery(shell, SWT.V_SCROLL | SWT.MULTI);

		// Renderers
		DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(/*56*/ 90);
		gr.setItemWidth(/*72*/ 100);
		gr.setAutoMargin(true);
		gallery.setGroupRenderer(gr);

		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		gallery.setItemRenderer(ir);

		for (int g = 0; g < 3; g++) {
			GalleryItem group = new GalleryItem(gallery, SWT.NONE);
			group.setText("Group " + g); //$NON-NLS-1$
			group.setExpanded(true);

			for (int i = 0; i < 7; i++) {
				GalleryItem item = new GalleryItem(group, SWT.NONE);
				if (itemImage != null) {
					item.setImage(itemImage);
				}
				item.setText("Item " + i); //$NON-NLS-1$
			}
		}
	}
}