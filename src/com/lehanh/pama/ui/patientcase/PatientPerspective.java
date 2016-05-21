package com.lehanh.pama.ui.patientcase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
//import org.eclipse.ui.IFolderLayout;
//import org.eclipse.ui.IPageLayout;
//import org.eclipse.ui.IPartListener2;
//import org.eclipse.ui.IPerspectiveFactory;
//import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.ui.IWorkbenchPart;
//import org.eclipse.ui.IWorkbenchPartReference;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.PlatformUI;

import com.lehanh.pama.ui.PerspectiveSwitcherToolbar;

public class PatientPerspective /*implements IPerspectiveFactory, IPartListener2*/ {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "com.lehanh.pama.patientPerspective";

//	public void createInitialLayout(IPageLayout layout) {
//		String editorArea = layout.getEditorArea();
//		layout.setEditorAreaVisible(false);
//		
//	    IFolderLayout folder = layout.createFolder("view", IPageLayout.TOP , 1, editorArea);
//	    folder.addView(PatientInfoView.ID);
//	    folder.addView(PatientCaseView.ID);
//	    folder.addView(DetailImageView.ID);
//	    folder.addView(PrescriptionView.ID);
//	    
//	    layout.getViewLayout(PatientInfoView.ID).setCloseable(false);
//	    layout.getViewLayout(PatientCaseView.ID).setCloseable(false);
//	    layout.getViewLayout(DetailImageView.ID).setCloseable(false);
//	    layout.getViewLayout(PrescriptionView.ID).setCloseable(false);
//	    
//		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		window.getPartService().addPartListener(this);
//	}
//
//	@Override
//	public void partDeactivated(IWorkbenchPartReference partRef) {
//		PerspectiveSwitcherToolbar.paDeaView = null;
//		
//		IWorkbenchPart partDea = partRef.getPart(false);
//		if (!(partDea instanceof IPatientView)) {
//			return;
//		}
//		
//		IPatientView paDeaView = (IPatientView) partDea;
//		if (paDeaView.isEditing()) {
//			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//			IWorkbenchPage page = null;
//			if (window != null) {
//				page = window.getActivePage();
//				// TODO handle out of perspective page.setPerspective(PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(ID));
//				page.activate(partDea);
//			}
//			
//			MessageBox dialog = 
//				new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR | SWT.OK);
//			dialog.setText("Lổi nhập liệu");
//			dialog.setMessage("Phải hoàn chỉnh việc nhập liệu trên \"" + partDea.getTitle() + "\"trước khi qua màn hình khác!");
//			dialog.open();
//		}
//	}
//
//	@Override
//	public void partActivated(IWorkbenchPartReference partRef) {
//		IWorkbenchPart partDea = partRef.getPart(false);
//		if (!(partDea instanceof IPatientView)) {
//			return;
//		}
//		
//		IPatientView paDeaView = (IPatientView) partDea;
//		PerspectiveSwitcherToolbar.paDeaView = paDeaView;
//	}
//	
//	@Override
//	public void partBroughtToTop(IWorkbenchPartReference partRef) {
//		// do nothing
//	}
//
//	@Override
//	public void partClosed(IWorkbenchPartReference partRef) {
//		// do nothing
//	}
//
//	@Override
//	public void partOpened(IWorkbenchPartReference partRef) {
//		// do nothing
//	}
//
//	@Override
//	public void partHidden(IWorkbenchPartReference partRef) {
//		// do nothing
//	}
//
//	@Override
//	public void partVisible(IWorkbenchPartReference partRef) {
//		// do nothing
//	}
//
//	@Override
//	public void partInputChanged(IWorkbenchPartReference partRef) {
//		// do nothing
//	}
}
