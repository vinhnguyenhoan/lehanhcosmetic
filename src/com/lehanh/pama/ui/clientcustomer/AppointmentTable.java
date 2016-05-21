package com.lehanh.pama.ui.clientcustomer;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolItem;

import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.patientcase.AppointmentSchedule;
import com.lehanh.pama.patientcase.IPatientManager;
import com.lehanh.pama.ui.E4LifeCycle;
import com.lehanh.pama.ui.patientcase.PatientPerspectiveHandler;
import com.lehanh.pama.ui.util.ACommonComboViewer;
import com.lehanh.pama.util.PamaHome;

class AppointmentTable extends ACommonComboViewer {

	private TableViewer tableViewer;

	AppointmentTable(final String uid, Composite composite_1) {
		this.tableViewer = new TableViewer(composite_1, SWT.H_SCROLL
		        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		newColDef(SWT.LEFT, Messages.AppointmentTable_thongtinkhach);
		newColDef(SWT.LEFT, Messages.AppointmentTable_hen);
		newColDef(SWT.LEFT, Messages.AppointmentTable_ghichu);
		
		this.tableViewer.setContentProvider(this);
		// set the label providers
		this.tableViewer.setLabelProvider(this);
		pack();
		
		final IPatientManager paManager = (IPatientManager) PamaHome.getService(IPatientManager.class);

		// initial actions
		this.tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				AppointmentSchedule model = (AppointmentSchedule) ((IStructuredSelection) event.getSelection()).getFirstElement();
				try {
					paManager.selectPatient(uid, model.getPatientId());
				} catch (SQLException | ParseException e) {
					e.printStackTrace();
					return;
				}

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
	
	private void pack() {
		for (TableColumn col : this.tableViewer.getTable().getColumns()) {
			col.pack();
		}
	}

	private void newColDef(int style, String text) {
		TableColumn sttCol = new TableColumn(tableViewer.getTable(), SWT.CENTER);
		sttCol.setText(text);
	}

	Table getTable() {
		return this.tableViewer.getTable();
	}

	@SuppressWarnings("unchecked")
	List<AppointmentSchedule> getInput() {
		return (List<AppointmentSchedule>) this.tableViewer.getInput();
	}
	
//	@Override
//	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AppointmentSchedule[] getElements(Object inputElement) {
		if (inputElement == null) {
			return null;
		}
    	if (inputElement instanceof List) {
    		List<AppointmentSchedule> coll = (List<AppointmentSchedule>) inputElement;
			return coll.toArray(new AppointmentSchedule[coll.size()]);
		}
        return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		AppointmentSchedule model = (AppointmentSchedule) element;
		// newColDef(SWT.LEFT, "Thông tin khách");
		// newColDef(SWT.LEFT, "Hẹn");
		// newColDef(SWT.LEFT, "Ghi chú");
		Catagory appT = model.getAppointmentCatagory();
		switch (columnIndex) {
			case 0: return StringUtils.isBlank(model.getPaInfo()) ? StringUtils.EMPTY : model.getPaInfo();
			case 1: return appT == null ? StringUtils.EMPTY : appT.getDesc();
			case 2: return StringUtils.isBlank(model.getNote()) ? StringUtils.EMPTY : model.getNote();
		}
		return ""; //$NON-NLS-1$
	}

	void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.tableViewer.addSelectionChangedListener(listener);
	}

	void setInput(List<AppointmentSchedule> data) {
		this.tableViewer.setInput(data);
	}

}