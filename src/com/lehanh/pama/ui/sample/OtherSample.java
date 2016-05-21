package com.lehanh.pama.ui.sample;

import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.lehanh.pama.ui.util.PamaResourceManager;


public class OtherSample {
	public static void main(String[] args) {
		Display display = new Display();
//		Image itemImage = new Image(display, Program
//				.findProgram("jpg").getImageData()); //$NON-NLS-1$
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		Image itemImage = PamaResourceManager.getImage("C:\\Users\\vinhhnguyen\\Desktop\\mui_3 - Copy.jpg"/*, 120, 150*/); // 17176 - Copy
		
		Gallery gallery = new Gallery(shell, SWT.V_SCROLL | SWT.MULTI);
		// Renderers
		DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(itemImage.getImageData().height);
		gr.setItemWidth(itemImage.getImageData().width);
		gr.setAutoMargin(false);
//		gr.setAlwaysExpanded(true);
		gallery.setGroupRenderer(gr);

		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		gallery.setItemRenderer(ir);

		GalleryItem group = new GalleryItem(gallery, SWT.NONE);
		group.setText("Group " + 1); //$NON-NLS-1$
		group.setExpanded(true);

		for (int i = 0; i < 2; i++) {
			GalleryItem item = new GalleryItem(group, SWT.NONE);
			if (itemImage != null) {
				item.setImage(itemImage);
			}
//			item.setText("Item " + i); //$NON-NLS-1$
		}

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
	
	public static void main4(String[] args) {
		Display display = new Display();
//		Image itemImage = new Image(display, Program
//				.findProgram("jpg").getImageData()); //$NON-NLS-1$
		Image itemImage = PamaResourceManager.getImage("C:\\Users\\vinhhnguyen\\Desktop\\mui_3 - Copy.jpg"/*, 120, 150*/); // 17176 - Copy
		
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Gallery gallery = new Gallery(shell, SWT.V_SCROLL | SWT.MULTI);

		// Renderers
		DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(100);
		gr.setItemWidth(72);
		gr.setAutoMargin(true);
//		gr.setAlwaysExpanded(true);
		gallery.setGroupRenderer(gr);

		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		gallery.setItemRenderer(ir);

		for (int g = 0; g < 2; g++) {
			GalleryItem group = new GalleryItem(gallery, SWT.NONE);
			group.setText("Group " + g); //$NON-NLS-1$
			group.setExpanded(true);

			for (int i = 0; i < 50; i++) {
				GalleryItem item = new GalleryItem(group, SWT.NONE);
				if (itemImage != null) {
					item.setImage(itemImage);
				}
				item.setText("Item " + i); //$NON-NLS-1$
			}
		}

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
	
	public static final void main2(String[] args) {
		final int COLUMN_COUNT = 4;
		final int ITEM_COUNT = 8;
		final int TEXT_MARGIN = 3;
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		for (int i = 0; i < COLUMN_COUNT; i++) {
			new TableColumn(table, SWT.NONE);
		}
		for (int i = 0; i < ITEM_COUNT; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			for (int j = 0; j < COLUMN_COUNT; j++) {
				String string = "item " + i + " col " + j;
				if ((i + j) % 3 == 1) {
					string += "\nnew line1";
				}
				if ((i + j) % 3 == 2) {
					string += "\nnew line1\nnew line2";
				}
				item.setText(j, string);
			}
		}
		table.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				TableItem item = (TableItem) event.item;
				String text = item.getText(event.index);
				Point size = event.gc.textExtent(text);
				event.width = size.x + 2 * TEXT_MARGIN;
				//event.height = Math.max(event.height, size.y + TEXT_MARGIN) * 3;
				event.height = 150;
			}
		});
		table.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				event.detail &= ~SWT.FOREGROUND;
			}
		});
		table.addListener(SWT.PaintItem, new Listener() {
			public void handleEvent(Event event) {
				TableItem item = (TableItem) event.item;
				String text = item.getText(event.index);
				Image image = item.getImage(event.index);
				
				/* center column 1 vertically */
				int yOffset = 0;
				if (event.index == 1) {
					Point size = event.gc.textExtent(text);
					yOffset = Math.max(0, (event.height - size.y) / 2);
				}
				event.gc.drawText(text, event.x + TEXT_MARGIN, event.y + yOffset, true);
			}
		});
		for (int i = 0; i < COLUMN_COUNT; i++) {
			table.getColumn(i).pack();
		}
		table.pack();
		table.layout();
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	static int count = 1;
	public static final void main3(String[] args) {
		final int COLUMN_COUNT = 4;
		final int ITEM_COUNT = 5;
		final int TEXT_MARGIN = 3;
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		
//		final Image image = SWTResourceManager.getImage("C:\\Users\\vinhhnguyen\\Desktop\\17176 - Copy.jpg"/*, 120, 150*/);
//		final ImageData size = image.getImageData();
		
		for (int i = 0; i < COLUMN_COUNT; i++) {
			new TableColumn(table, SWT.NONE);
		}
		for (int i = 0; i < ITEM_COUNT; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			for (int j = 0; j < COLUMN_COUNT; j++) {
//				item.setData(SWTResourceManager.getImage("C:\\Users\\vinhhnguyen\\Desktop\\17176 - Copy.jpg"/*, 120, 150*/));
				item.setImage(j, SWTResourceManager.getImage("C:\\Users\\vinhhnguyen\\Desktop\\17176 - Copy.jpg"/*, 120, 150*/));
//				String string = "item " + i + " col " + j;
//				if ((i + j) % 3 == 1) {
//					string += "\nnew line1";
//				}
//				if ((i + j) % 3 == 2) {
//					string += "\nnew line1\nnew line2";
//				}
//				item.setText(j, string);
			}
		}
		table.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				TableItem item = (TableItem) event.item;
				Image image = item.getImage(event.index);
//				Image image = (Image) item.getData();
				ImageData size = image.getImageData();
				event.width = size.width + 2 * TEXT_MARGIN;
				event.height = Math.max(event.height, size.height + TEXT_MARGIN);
			}
		});
		table.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				event.detail &= ~SWT.FOREGROUND;
			}
		});
		
		
		table.addListener(SWT.PaintItem, new Listener() {
			public void handleEvent(Event event) {
                System.out.println("PaintItem. " + count++);

				TableItem item = (TableItem) event.item;
				Image image = item.getImage(event.index);
//				Image image = (Image) item.getData();
//				Image image = item.getImage(event.index);
				/* center column 1 vertically */
				int yOffset = 0;
//				if (event.index == 1) {
//					ImageData size = image.getImageData();
//					yOffset = Math.max(0, (event.height - size.y) / 2);
//				}
				event.gc.drawImage(image, event.x + TEXT_MARGIN, event.y);
//				event.gc.drawText("drawText", event.x + TEXT_MARGIN, event.y + yOffset, true);

			}
		});
		
		showCursor(table);
		
		
		for (int i = 0; i < COLUMN_COUNT; i++) {
			table.getColumn(i).pack();
		}
		table.pack();
		shell.pack();
		shell.open();
		table.layout(true, true);
		shell.layout(true, true);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void showCursor(final Table table) {
		// create a TableCursor to navigate around the table
				final TableImageCursor cursor = new TableImageCursor(table, SWT.NONE);
				// create an editor to edit the cell when the user hits "ENTER"
				// while over a cell in the table
//				final ControlEditor editor = new ControlEditor(cursor);
//				editor.grabHorizontal = true;
//				editor.grabVertical = true;

				/*cursor.addSelectionListener(new SelectionAdapter() {
					// when the TableEditor is over a cell, select the corresponding row in the table
					public void widgetSelected(SelectionEvent e) {
						table.setSelection(new TableItem[] { cursor.getRow() });
					}

					// when the user hits "ENTER" in the TableCursor, pop up a text
					// editor so that
					// they can change the text of the cell
					public void widgetDefaultSelected(SelectionEvent e) {
						final Text text = new Text(cursor, SWT.NONE);
						TableItem row = cursor.getRow();
						int column = cursor.getColumn();
						text.setText(row.getText(column));
						text.addKeyListener(new KeyAdapter() {
							public void keyPressed(KeyEvent e) {
								// close the text editor and copy the data over
								// when the user hits "ENTER"
								if (e.character == SWT.CR) {
									TableItem row = cursor.getRow();
									int column = cursor.getColumn();
									row.setText(column, text.getText());
									text.dispose();
								}
								// close the text editor when the user hits "ESC"
								if (e.character == SWT.ESC) {
									text.dispose();
								}
							}
						});
						editor.setEditor(text);
						text.setFocus();
					}
				});*/
				// Hide the TableCursor when the user hits the "CTRL" or "SHIFT" key.
				// This alows the user to select multiple items in the table.
				cursor.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						if (e.keyCode == SWT.CTRL || e.keyCode == SWT.SHIFT || (e.stateMask & SWT.CONTROL) != 0
								|| (e.stateMask & SWT.SHIFT) != 0) {
							cursor.setVisible(false);
						}
					}
				});
				// Show the TableCursor when the user releases the "SHIFT" or "CTRL" key.
				// This signals the end of the multiple selection task.
				table.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent e) {
						if (e.keyCode == SWT.CONTROL && (e.stateMask & SWT.SHIFT) != 0)
							return;
						if (e.keyCode == SWT.SHIFT && (e.stateMask & SWT.CONTROL) != 0)
							return;
						if (e.keyCode != SWT.CONTROL && (e.stateMask & SWT.CONTROL) != 0)
							return;
						if (e.keyCode != SWT.SHIFT && (e.stateMask & SWT.SHIFT) != 0)
							return;

						TableItem[] selection = table.getSelection();
						TableItem row = (selection.length == 0) ? table.getItem(table.getTopIndex()) : selection[0];
						table.showItem(row);
						cursor.setSelection(row, 0);
						cursor.setVisible(true);
						cursor.setFocus();
					}
				});
	}

}
