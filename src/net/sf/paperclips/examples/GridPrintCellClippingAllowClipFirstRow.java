/*
 * Copyright (c) 2009 Matthew Hall and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Matthew Hall - initial API and implementation
 */
package net.sf.paperclips.examples;

import net.sf.paperclips.DefaultGridLook;
import net.sf.paperclips.GridPrint;
import net.sf.paperclips.LineBorder;
import net.sf.paperclips.PaperClips;
import net.sf.paperclips.Print;
import net.sf.paperclips.PrintJob;
import net.sf.paperclips.TextPrint;
import net.sf.paperclips.ui.PrintPreview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class GridPrintCellClippingAllowClipFirstRow {
	public static void main(String[] args) {
		final Display display = new Display();

		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new GridLayout());
		shell.setSize(600, 600);

		PrintJob job = new PrintJob("GridPrintCellClippingAllowClipFirstRow",
				createPrint());

		Composite buttons = new Composite(shell, SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		buttons.setLayout(new RowLayout(SWT.HORIZONTAL));

		final Button prev = new Button(buttons, SWT.PUSH);
		prev.setText("<< Prev");
		prev.setEnabled(false);

		final Button next = new Button(buttons, SWT.PUSH);
		next.setText("Next >>");

		final PrintPreview preview = new PrintPreview(shell, SWT.BORDER);
		preview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		preview.setPrintJob(job);
		next.setEnabled(preview.getPageCount() > 1);

		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				int newIndex = preview.getPageIndex();
				if (event.widget == prev)
					newIndex--;
				else
					newIndex++;
				preview.setPageIndex(newIndex);
				prev.setEnabled(newIndex > 0);
				next.setEnabled(newIndex < preview.getPageCount() - 1);
			}
		};
		prev.addListener(SWT.Selection, listener);
		next.addListener(SWT.Selection, listener);

		shell.open();

		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();

		PaperClips.print(job, new PrinterData());
	}

	public static Print createPrint() {
		GridPrint doc = new GridPrint("d:g, d:g", new DefaultGridLook(5, 2));

		doc.addHeader(new TextPrint("Cell clipping enabled"));
		doc.add(createGrid(true));

		doc.addHeader(new TextPrint("Cell clipping disabled"));
		doc.add(createGrid(false));
		return doc;
	}

	public static GridPrint createGrid(boolean cellClippingEnabled) {
		DefaultGridLook look = new DefaultGridLook();
		look.setCellBorder(new LineBorder());
		GridPrint grid = new GridPrint("d", look);
		grid.setCellClippingEnabled(cellClippingEnabled);

		String longText = "The quick brown fox jumps over the lazy dog.";
		for (int i = 0; i < 5; i++)
			// double 7 times -> repeated 128 times
			longText += "  " + longText;

		for (int i = 0; i < 5; i++)
			grid.add(new TextPrint(longText));
		return grid;
	}
}
