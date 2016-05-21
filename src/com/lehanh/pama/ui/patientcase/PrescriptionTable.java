package com.lehanh.pama.ui.patientcase;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.lehanh.pama.catagory.PrescriptionItem;
import com.lehanh.pama.ui.util.ACommonComboViewer;

class PrescriptionTable extends ACommonComboViewer {

	private TableViewer tableViewer;

	PrescriptionTable(Composite composite_1) {
		this.tableViewer = new TableViewer(composite_1, SWT.H_SCROLL
		        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		//newColDef(SWT.CENTER, "STT");
		newColDef(SWT.LEFT, Messages.PrescriptionTable_tenthuoc);
		newColDef(SWT.LEFT, Messages.PrescriptionTable_tengoc);
		newColDef(SWT.RIGHT, Messages.PrescriptionTable_songay);
		newColDef(SWT.LEFT, Messages.PrescriptionTable_donvi);
		newColDef(SWT.RIGHT, Messages.PrescriptionTable_ngaydung);
		newColDef(SWT.RIGHT, Messages.PrescriptionTable_landung);
		newColDef(SWT.LEFT, Messages.PrescriptionTable_cachdung);
		newColDef(SWT.LEFT, Messages.PrescriptionTable_cuuong);
		newColDef(SWT.LEFT, Messages.PrescriptionTable_luuy);
		
		this.tableViewer.setContentProvider(this);
		// set the label providers
		this.tableViewer.setLabelProvider(this);
		pack();
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
	List<PrescriptionItem> getInput() {
		return (List<PrescriptionItem>) this.tableViewer.getInput();
	}
	
//	@Override
//	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PrescriptionItem[] getElements(Object inputElement) {
		if (inputElement == null) {
			return null;
		}
    	if (inputElement instanceof List) {
    		List<PrescriptionItem> coll = (List<PrescriptionItem>) inputElement;
			return coll.toArray(new PrescriptionItem[coll.size()]);
		}
        return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		PrescriptionItem model = (PrescriptionItem) element;
		// newColDef(SWT.LEFT, "Tên thuốc");
		// newColDef(SWT.LEFT, "Tên gốc");
		// newColDef(SWT.RIGHT, "Số ngày");
		// newColDef(SWT.LEFT, "Đơn vị");
		// newColDef(SWT.RIGHT, "Ngày dùng");
		// newColDef(SWT.RIGHT, "Lần dùng");
		// newColDef(SWT.LEFT, "Cách dùng");
		// newColDef(SWT.LEFT, "Cử uống");
		// newColDef(SWT.LEFT, "Lưu ý");
		switch (columnIndex) {
			case 0: return model.getDrug();
			case 1: return model.getDrugDesc();
			case 2: return String.valueOf(model.getNumDay());
			case 3: return model.getUnit();
			case 4: return String.valueOf(model.getPerSs());
			case 5: return model.getUse();
			case 6: return model.getSs();
			case 7: return model.getNote();
		}
		return ""; //$NON-NLS-1$
	}

	void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.tableViewer.addSelectionChangedListener(listener);
	}

	void setInput(List<PrescriptionItem> data) {
		this.tableViewer.setInput(data);
	}

	void updateLine(PrescriptionItem currentItem) {
		@SuppressWarnings("unchecked")
		List<PrescriptionItem> data = (List<PrescriptionItem>) this.tableViewer.getInput();
		if (data == null) {
			data = new LinkedList<PrescriptionItem>();
			data.add(currentItem);
			setInput(data);
			return;
		}
		
		int index = 0;
		for (PrescriptionItem item : data) {
			if (item.getDrug().equalsIgnoreCase(currentItem.getDrug())) {
				break;
			}
			index++;
		}
		if (index > data.size() - 1) {
			data.add(currentItem);
		} else {
			data.set(index, currentItem);
		}
		setInput(data);
	}

	void delete(String drug) {
		@SuppressWarnings("unchecked")
		List<PrescriptionItem> data = (List<PrescriptionItem>) this.tableViewer.getInput();
		if (data == null || data.isEmpty()) {
			return;
		}
		
		int index = 0;
		for (PrescriptionItem item : data) {
			if (item.getDrug().equalsIgnoreCase(drug)) {
				break;
			}
			index++;
		}
		if (index < data.size()) {
			data.remove(index);
		}
		setInput(data);
	}

}