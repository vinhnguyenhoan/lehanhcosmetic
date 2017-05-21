package com.lehanh.pama.ui.clientcustomer;

import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolItem;

import com.lehanh.pama.patientcase.IPatientManager;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.ui.E4LifeCycle;
import com.lehanh.pama.ui.patientcase.PatientPerspectiveHandler;
import com.lehanh.pama.ui.util.TableDataProvider;
import com.lehanh.pama.util.DateUtils;
import com.lehanh.pama.util.PamaHome;

class PatientTable extends TableDataProvider {

	PatientTable(final String uid, Composite composite_1) {
		super(composite_1);
		
		newColDef(SWT.LEFT, "STT", 10);
		newColDef(SWT.LEFT, Messages.PatientTable_soid, 20);
		newColDef(SWT.LEFT, Messages.PatientTable_tenkhach, 60);
		newColDef(SWT.CENTER, Messages.PatientTable_ngaysinh, 30);
		newColDef(SWT.LEFT, Messages.PatientTable_didong, 40);
		newColDef(SWT.LEFT, Messages.PatientTable_diachi, 40);
		newColDef(SWT.CENTER, Messages.PatientTable_gioitinh, 30);
		
		final IPatientManager paManager = (IPatientManager) PamaHome.getService(IPatientManager.class);
		// initial actions
		this.getTableViewer().addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Patient model = (Patient) ((IStructuredSelection) event.getSelection()).getFirstElement();
				paManager.selectPatient(uid, model);

				final EModelService modelService = E4LifeCycle.workbenchContext.get(EModelService.class);
				MApplication app = E4LifeCycle.workbenchContext.get(MApplication.class);
				EPartService partService = E4LifeCycle.workbenchContext.get(EPartService.class);
				
		        List<MPerspective> perspectives = modelService.findElements(app, PatientPerspectiveHandler.ID, MPerspective.class, null);
		        partService.switchPerspective(perspectives.get(0));

		        List<MToolItem> toolItem = modelService.findElements(app, "lehanhcosmetic.patient", MToolItem.class, null); //$NON-NLS-1$
		        ((ToolItem) toolItem.get(0).getWidget()).setSelection(true);
		        
		        toolItem = modelService.findElements(app, "lehanhcosmetic.clientcustomer", MToolItem.class, null); //$NON-NLS-1$
		        ((ToolItem) toolItem.get(0).getWidget()).setSelection(false);
			}
		});
		
		/*Menu popupMenu = new Menu(tableViewer.getTable());
	    
	    MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
	    deleteItem.setText("Xóa ảnh");
	    deleteItem.setImage(PamaHome.application.loadImage("icons/symbol_delete.png"));
	    deleteItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteSelectedItems();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				deleteSelectedItems();
			}
		});
	    
	    MenuItem linkItem = new MenuItem(popupMenu, SWT.NONE);
	    linkItem.setText("Thêm ảnh before - after");
	    linkItem.setImage(PamaHome.application.loadImage("icons/insert_link.png"));
	    linkItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				linkItems();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				linkItems();
			}
		});*/
	}
	
	Table getTable() {
		return this.getTableViewer().getTable();
	}

	@SuppressWarnings("unchecked")
	List<Patient> getInput() {
		return (List<Patient>) this.getTableViewer().getInput();
	}
	
//	@Override
//	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Patient[] getElements(Object inputElement) {
		if (inputElement == null) {
			return null;
		}
    	if (inputElement instanceof List) {
    		List<Patient> coll = (List<Patient>) inputElement;
			return coll.toArray(new Patient[coll.size()]);
		}
        return null;
	}
	
	private int startIndex = 1;
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		Patient model = (Patient) element;
		/*switch (columnIndex) {
			case 0: return String.valueOf(model.getId());
			case 1: return model.getName();
			case 2: return DateUtils.convertDateDataType(model.getBirthDay());
			case 3: return model.getCellPhone();
			case 4: return model.getAddress();
			case 5: return model.getSex();
		}*/
		switch (columnIndex) {
			case 0: return String.valueOf(startIndex++);
			case 1: return String.valueOf(model.getId());
			case 2: return model.getName();
			case 3: return DateUtils.convertDateDataType(model.getBirthDay());
			case 4: return model.getCellPhone();
			case 5: return model.getAddress();
			case 6: return model.getSex();
		}
		return ""; //$NON-NLS-1$
	}

	void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.getTableViewer().addSelectionChangedListener(listener);
	}

	void setInput(List<Patient> data) {
		this.getTableViewer().setInput(data);
		this.startIndex = 1;
		//pack();
	}

	/*private void pack() {
		for (TableColumn col : this.tableViewer.getTable().getColumns()) {
			col.pack();
		}
	}*/

}