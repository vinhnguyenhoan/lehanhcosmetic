package com.lehanh.pama.ui.patientcase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

import com.lehanh.pama.ui.util.PamaResourceManager;

public class ImageCanvasPainter implements PaintListener {

//	private static final int START_Y = 5;
//	private static final int START_X = 5;

	private final Canvas canvas;
	private Image image;
	
	private final StartPoint origin = new StartPoint();
	
	private class StartPoint {
	
		private final Point point;
		private static final boolean DEBUG = false;
		
		StartPoint() {
			point = new Point(0, 0);
		}
		
		private void x(int x, String... debugsId) {
			if (DEBUG) System.out.println("x: " + x + " from " + Arrays.toString(debugsId));
			point.x = x;
		}
		
		private void y(int y, String... debugsId) {
			if (DEBUG) System.out.println("y: " + y + " from " + Arrays.toString(debugsId));
			point.y = y;
		}

		private int x() {
			return point.x;
		}
		
		private int y() {
			return point.y;
		}

		private void centerX(String... debugIds) {
			if (DEBUG) System.out.println("Centerx");
			x((canvas.getClientArea().width / 2) - (image.getBounds().width / 2), debugIds);
		}

		private void centerY(String... debugIds) {
			if (DEBUG) System.out.println("CenterY");
			y((canvas.getClientArea().height / 2) - (image.getBounds().height / 2), debugIds);
		}
	}
	
	public ImageCanvasPainter(Composite composite) {
		this(new Canvas(composite, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE
				| SWT.V_SCROLL | SWT.H_SCROLL));
	}
	
	private ImageCanvasPainter(Canvas canvasInput) {
		this.canvas = canvasInput;
		this.canvas.addPaintListener(this);
		final ScrollBar hBar = canvas.getHorizontalBar();
		hBar.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (image == null || image.isDisposed()) {
					return;
				}
				int hSelection = hBar.getSelection();
				int destX = -hSelection - origin.x();
				Rectangle rect = image.getBounds();
				canvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
				origin.x(-hSelection, "hBar Selection");
			}
		});

		final ScrollBar vBar = canvas.getVerticalBar();
		vBar.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (image == null || image.isDisposed()) {
					return;
				}
				int vSelection = vBar.getSelection();
				int destY = -vSelection - origin.y();
				Rectangle rect = image.getBounds();
				canvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
				origin.y(-vSelection, "vBar Selection");
			}
		});

		// TODO handle mouse scroll wheel 
		canvas.addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (image == null || image.isDisposed()) {
					return;
				}
				Rectangle rect = image.getBounds();
				Rectangle client = canvas.getClientArea();
				hBar.setMaximum(rect.width);
				vBar.setMaximum(rect.height);
				hBar.setThumb(Math.min(rect.width, client.width));
				vBar.setThumb(Math.min(rect.height, client.height));
				int hPage = rect.width - client.width;
				int vPage = rect.height - client.height;
				int hSelection = hBar.getSelection();
				int vSelection = vBar.getSelection();
				if (hSelection >= hPage) {
					if (hPage <= 0) {
						//hSelection = 0;
						//origin.x(0, ".Resize");
						origin.centerX(".Resize");
					} else {
						origin.x(-hSelection, ".Resize");
					}
				}
				if (vSelection >= vPage) {
					if (vPage <= 0) {
						//vSelection = 0;
						//origin.y(0, ".Resize");
						origin.centerY(".Resize");
					} else {
						origin.y(-vSelection, ".Resize");
					}
				}
				canvas.redraw();
			}
		});
	}

	public void viewImage(List<GalleryItemData> imagePath) {
		if (this.image != null && !this.image.isDisposed()) {
			this.image.dispose();
		}
		try {
			this.image = reloadImage(imagePath, this.canvas.getDisplay(), this.canvas.getClientArea());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		origin.centerX(".viewImage");
		origin.centerY(".viewImage");
		Event event = new Event();
		event.type = SWT.Resize;
		canvas.notifyListeners(SWT.Resize, event);
	}

	private static Image reloadImage(List<GalleryItemData> imagePaths, Display display, Rectangle clientRect) throws FileNotFoundException, IOException {
		if (display.isDisposed()) {
			return null;
		}
		
		Collections.sort(imagePaths);
		
		int totalW = 0;
		int totalH = 0;
		List<Image> scaledImages = new LinkedList<Image>();
		Image currI;
		for (GalleryItemData iD : imagePaths) {
			currI = PamaResourceManager.getImage(StringUtils.EMPTY, iD.fullPath(), -1, clientRect.height);
			scaledImages.add(currI);
			totalW += currI.getBounds().width;
			totalH = Math.max(totalH, currI.getBounds().height);
		}
		
		Image image = new Image(display, totalW, totalH);
		final GC newGC = new GC(image);
		newGC.setForeground(PamaResourceManager.getColor(SWT.COLOR_DARK_RED));
		newGC.setFont(PamaResourceManager.getFont("Segoe UI", 18, SWT.ITALIC | SWT.BOLD));
		
		int x = 0;
		int y = 0;
		int indexImg = 0;
		boolean isMultiImage = imagePaths.size() > 1;
		for (GalleryItemData iD : imagePaths) {
			Image currImg = scaledImages.get(indexImg++);
			// draw image
			newGC.drawImage(currImg, x, y);
			// draw notice text
			String notice = "Before";
			if (iD.afterDays > 0) {
				notice = "After " + iD.afterDays + " days";
			}
			
			if (isMultiImage && notice != null) {
				int widthT = newGC.getFontMetrics().getAverageCharWidth() * notice.length();
				newGC.drawText(notice, x + ((currImg.getBounds().width/2) - (widthT/2)), currImg.getBounds().height - newGC.getFontMetrics().getHeight() * 2, true);
			}
			// pos x for next image
			x += currImg.getBounds().width;
		}
		newGC.dispose();
		return image;
	}

	@Override
	public void paintControl(PaintEvent e) {
		GC gc = e.gc;
		gc.setBackground(PamaResourceManager.getColor(SWT.COLOR_WHITE));
		gc.fillRectangle(0, 0, canvas.getClientArea().width, canvas.getClientArea().height);
		
		if (image == null || image.isDisposed()) {
			return;
		}
		gc.drawImage(image, origin.x(), origin.y());
//		Rectangle rect = image.getBounds();
//		Rectangle client = canvas.getClientArea();
//		int marginWidth = client.width - rect.width;
//		if (marginWidth > 0) {
//			gc.fillRectangle(rect.width, 0, marginWidth, client.height);
//		}
//		int marginHeight = client.height - rect.height;
//		if (marginHeight > 0) {
//			gc.fillRectangle(0, rect.height, client.width, marginHeight);
//		}
	}

	public void clearImages() {
		this.image = null;
		this.canvas.redraw();
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	
//	public static void main(String[] args) {
//		Display display = new Display();
//		Shell shell = new Shell(display);
//		shell.setLayout(new FillLayout());
//		Image originalImage = null;
//		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
//		dialog.setText("Open an image file or cancel");
//		String string = dialog.open();
//		if (string != null) {
//			originalImage = new Image(display, string);
//		}
//		if (originalImage == null) {
//			int width = 150, height = 200;
//			originalImage = new Image(display, width, height);
//			GC gc = new GC(originalImage);
//			gc.fillRectangle(0, 0, width, height);
//			gc.drawLine(0, 0, width, height);
//			gc.drawLine(0, height, width, 0);
//			gc.drawText("Default Image", 10, 10);
//			gc.dispose();
//		}
//		final Image image = originalImage;
//		final Point origin = new Point(0, 0);
//		final Canvas canvas = new Canvas(shell, SWT.NO_BACKGROUND
//				| SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
//		final ScrollBar hBar = canvas.getHorizontalBar();
//		hBar.addListener(SWT.Selection, new Listener() {
//
//			@Override
//			public void handleEvent(Event e) {
//				int hSelection = hBar.getSelection();
//				int destX = -hSelection - origin.x;
//				Rectangle rect = image.getBounds();
//				canvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
//				origin.x = -hSelection;
//			}
//		});
//
//		final ScrollBar vBar = canvas.getVerticalBar();
//		vBar.addListener(SWT.Selection, new Listener() {
//
//			@Override
//			public void handleEvent(Event event) {
//				int vSelection = vBar.getSelection();
//				int destY = -vSelection - origin.y;
//				Rectangle rect = image.getBounds();
//				canvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
//				origin.y = -vSelection;
//			}
//		});
//
//		canvas.addListener(SWT.Resize, new Listener() {
//
//			@Override
//			public void handleEvent(Event event) {
//				Rectangle rect = image.getBounds();
//				Rectangle client = canvas.getClientArea();
//				hBar.setMaximum(rect.width);
//				vBar.setMaximum(rect.height);
//				hBar.setThumb(Math.min(rect.width, client.width));
//				vBar.setThumb(Math.min(rect.height, client.height));
//				int hPage = rect.width - client.width;
//				int vPage = rect.height - client.height;
//				int hSelection = hBar.getSelection();
//				int vSelection = vBar.getSelection();
//				if (hSelection >= hPage) {
//					if (hPage <= 0)
//						hSelection = 0;
//					origin.x = -hSelection;
//				}
//				if (vSelection >= vPage) {
//					if (vPage <= 0)
//						vSelection = 0;
//					origin.y = -vSelection;
//				}
//				canvas.redraw();
//			}
//		});
//		canvas.addListener(SWT.Paint, new Listener() {
//
//			@Override
//			public void handleEvent(Event e) {
//				GC gc = e.gc;
//				gc.drawImage(image, origin.x, origin.y);
//				Rectangle rect = image.getBounds();
//				Rectangle client = canvas.getClientArea();
//				int marginWidth = client.width - rect.width;
//				if (marginWidth > 0) {
//					gc.fillRectangle(rect.width, 0, marginWidth, client.height);
//				}
//				int marginHeight = client.height - rect.height;
//				if (marginHeight > 0) {
//					gc.fillRectangle(0, rect.height, client.width, marginHeight);
//				}
//			}
//		});
//		Rectangle rect = image.getBounds();
//		shell.setSize(Math.max(200, rect.width - 100),
//				Math.max(150, rect.height - 100));
//		shell.open();
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//		originalImage.dispose();
//		display.dispose();
//	}

}
