package com.lehanh.pama.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
//import org.eclipse.ui.IPerspectiveDescriptor;
//import org.eclipse.ui.IPerspectiveListener;
//import org.eclipse.ui.IWindowListener;
//import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.PlatformUI;

import com.lehanh.pama.ui.patientcase.IPatientView;

// TODO import com.jinnova.tradestation.ui.PerspectiveManager.PerspectiveUpdatedListener;

/**
 * Perspective Switcher Toolbar.
 * <p>
 * <b>Implementation Note:</b> this class assumes it is used only once per
 * window, so it only keeps track of one toolbar per shell.
 * </p>
 */
public class PerspectiveSwitcherToolbar extends ContributionItem /*TODO implements PerspectiveUpdatedListener*/ {

//	private static final String KEY_PERSPECTIVE_DESCR = "k_p_descr"; //$NON-NLS-1$
//
//	private static final IWindowListener WINDOW_LISTENER = new WindowListener();
//	private static final PerspectiveListener PERSPECTIVE_LISTENER = new PerspectiveListener();
//
//	private static final Map<Shell, ToolBar> TOOLBARS = new HashMap<Shell, ToolBar>();
//	private final SelectionListener toolbarListener = new SwitchPerspectiveToolbarListener();
//	private ToolBar parent;
//
//	public static IPatientView paDeaView;
//
//	static {
//		PlatformUI.getWorkbench().addWindowListener(WINDOW_LISTENER);
//	}
//
//	public PerspectiveSwitcherToolbar() {
//		this(null);
//	}
//
//	public PerspectiveSwitcherToolbar(String id) {
//		super(id);
//	}
//
//	@Override
//	public final boolean isDynamic() {
//		return true;
//	}
//
//	/**
//	 * Fills a toolbar with all available perspectives. The current one is
//	 * pressed.
//	 */
//	@Override
//	public void fill(final ToolBar parent, int index) {
//		this.parent = parent;
//		IPerspectiveDescriptor[] perspectives = PlatformUI.getWorkbench().getPerspectiveRegistry().getPerspectives();
//		for (IPerspectiveDescriptor tP : perspectives) {
//			newToolItem(tP);
//		}
//		TOOLBARS.put(parent.getShell(), parent);
//	}
//
//	private ToolItem newToolItem(IPerspectiveDescriptor descriptor) {
//		ToolItem item = new ToolItem(parent, SWT.RADIO);
//		item.setData(KEY_PERSPECTIVE_DESCR, descriptor);
//		item.addSelectionListener(toolbarListener);
//		/*if (descriptor.getPerspectiveD().getId().equals(PerspectiveManager.getInstance().getActivePage())) {
//			item.setSelection(true);
//			currentItem = item;
//		}*/
//		final Image image = descriptor.getImageDescriptor().createImage();
//		item.setImage(image);
//		String label = descriptor.getLabel();
//		item.setText(label);
//		item.addDisposeListener(new DisposeListener() {
//			public void widgetDisposed(DisposeEvent e) {
//				image.dispose();
//			}
//		});
//		return item;
//	}
//
//	/**
//	 * Switch perspective in the active page when the item is selected
//	 * (clicked).
//	 */
//	private static final class SwitchPerspectiveToolbarListener extends SelectionAdapter {
//		public void widgetSelected(SelectionEvent e) {
//			selectToolbarItem(e);
//		}
//	}
//
//	private static void selectToolbarItem(SelectionEvent e) {
//		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		ToolItem item = (ToolItem) e.widget;
//		IPerspectiveDescriptor perspectiveOfItem = (IPerspectiveDescriptor) item.getData(KEY_PERSPECTIVE_DESCR);
//		
//		if (paDeaView != null && paDeaView.isEditing()) {
//			e.doit = false;
//			IPerspectiveDescriptor perspective = window.getActivePage().getPerspective();
//			PERSPECTIVE_LISTENER.updateToolbar(window.getShell(), perspective);
//			if (perspective == perspectiveOfItem) {
//				MessageBox dialog = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR | SWT.OK);
//				dialog.setText("Lổi nhập liệu");
//				dialog.setMessage("Phải hoàn chỉnh việc nhập liệu trước khi qua màn hình khác!");
//				dialog.open();
//			}
//			return;
//		}
//
//		if (item.getSelection()) {
//			IWorkbenchPage page = null;
//			if (window != null) {
//				page = window.getActivePage();
//			}
//			if (page != null) {
//				page.setPerspective(perspectiveOfItem);
//			}
//		}
//		item.setSelection(true);
//	}
//	
//	/**
//	 * Update toolbar on activation of a perspective.
//	 * <p>
//	 * This listener listens to perspective activation on all windows and
//	 * updates the toolbar instance if necessary.
//	 */
//	private static final class PerspectiveListener implements IPerspectiveListener {
//		public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
//			// unused
//		}
//
//		public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
//			Shell shell = page.getWorkbenchWindow().getShell();
//			updateToolbar(shell, page.getPerspective());
//		}
//
//		public void updateToolbar(Shell shell, IPerspectiveDescriptor perspective) {
//			ToolItem[] items = getToolbarItem(shell);
//			if (items == null) {
//				return;
//			}
//			for (ToolItem item : items) {
//				boolean isSelected = perspective == item.getData(KEY_PERSPECTIVE_DESCR);
//				if (isSelected != item.getSelection()) {
//					item.setSelection(isSelected);
//				}
//			}
//		}
//	}
//
//	private static final class WindowListener implements IWindowListener {
//
//		public void windowActivated(IWorkbenchWindow window) {
//			// unused
//		}
//
//		public void windowClosed(IWorkbenchWindow window) {
//			TOOLBARS.remove(window.getShell());
//			window.removePerspectiveListener(PERSPECTIVE_LISTENER);
//		}
//
//		public void windowDeactivated(IWorkbenchWindow window) {
//			// unused
//		}
//
//		public void windowOpened(IWorkbenchWindow window) {
//			window.addPerspectiveListener(PERSPECTIVE_LISTENER);
//			// update toolbar 'selection' state when window opens, because the
//			// toolbar is
//			// created before the perspective. At this point we have the
//			// perspective.
//			Shell shell = window.getShell();
//			IPerspectiveDescriptor perspective = window.getActivePage().getPerspective();
//			PERSPECTIVE_LISTENER.updateToolbar(shell, perspective);
//		}
//	}
//
//	private static ToolItem[] getToolbarItem(Shell currentShell) {
//		ToolBar toolbar = TOOLBARS.get(currentShell);
//		if (toolbar == null) {
//			return null;
//		}
//		if (toolbar.isDisposed()) {
//			TOOLBARS.remove(currentShell);
//			return null;
//		}
//		return toolbar.getItems();
//	}

//	@Override
//	TODO public void perspectiveReset() {
//		currentItem = getCurrentItemPerspective();
//		if (currentItem != null && !currentItem.isDisposed()) {
//			currentItem.setSelection(true);
//		}
//	}

//	@Override
//	TODO public void perspectiveView() {
//		if (currentItem != null && !currentItem.isDisposed()) {
//			currentItem.setSelection(false);
//		}
//		PERSPECTIVE_LISTENER.updateToolbar(parent.getShell(), PerspectiveManager.getInstance().getActivePage()
//				.getPerspective());
//	}

}