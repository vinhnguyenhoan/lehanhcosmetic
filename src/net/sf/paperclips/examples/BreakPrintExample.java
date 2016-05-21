/*
 * Copyright (c) 2006 Matthew Hall and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Matthew Hall - initial API and implementation
 */
package net.sf.paperclips.examples;

import net.sf.paperclips.BorderPrint;
import net.sf.paperclips.BreakPrint;
import net.sf.paperclips.ColumnPrint;
import net.sf.paperclips.DefaultGridLook;
import net.sf.paperclips.GridPrint;
import net.sf.paperclips.LineBorder;
import net.sf.paperclips.PaperClips;
import net.sf.paperclips.Print;
import net.sf.paperclips.PrintJob;
import net.sf.paperclips.TextPrint;

import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;

/**
 * Prints "The quick brown fox jumps over the lazy dog." in increasingly large
 * blocks, using a BreakPrint every 5 blocks to force printing to advance to the
 * next column / page.
 * 
 * @author Matthew
 */
public class BreakPrintExample {
	public static Print createPrint() {
		GridPrint grid = new GridPrint("d:g", new DefaultGridLook(10, 10));

		String text = "The quick brown fox jumps over the lazy dog.";
		String printText = text;

		LineBorder border = new LineBorder();
		for (int i = 0; i < 15; i++, printText += "  " + text) {
			if (i > 0 && i % 5 == 0)
				grid.add(new BreakPrint());

			grid.add(new BorderPrint(new TextPrint(printText), border));
		}

		return new ColumnPrint(grid, 2, 10);
	}

	/**
	 * Prints the BreakPrintExample to the default printer.
	 * 
	 * @param args
	 *            command-line args
	 */
	public static void main(String[] args) {
		// Workaround for SWT bug on GTK - force SWT to initialize so we don't
		// crash.
		Display.getDefault();

		PaperClips.print(new PrintJob("BreakPrintExample.java", createPrint()),
				new PrinterData());
	}
}
