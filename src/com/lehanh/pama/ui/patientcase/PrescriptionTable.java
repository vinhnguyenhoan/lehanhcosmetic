package com.lehanh.pama.ui.patientcase;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.lehanh.pama.catagory.PrescriptionItem;
import com.lehanh.pama.ui.util.TableDataProvider;

class PrescriptionTable extends TableDataProvider {

	PrescriptionTable(Composite composite_1) {
		super(composite_1, new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));

		//newColDef(SWT.CENTER, "STT");
		newColDef(SWT.LEFT, Messages.PrescriptionTable_tenthuoc, 50);
		newColDef(SWT.LEFT, Messages.PrescriptionTable_tengoc, 30);
		newColDef(SWT.RIGHT, Messages.PrescriptionTable_songay, 20);
		newColDef(SWT.LEFT, Messages.PrescriptionTable_donvi, 20);
		newColDef(SWT.RIGHT, Messages.PrescriptionTable_ngaydung, 20);
		newColDef(SWT.RIGHT, Messages.PrescriptionTable_landung, 20);
		newColDef(SWT.LEFT, Messages.PrescriptionTable_cachdung, 35);
		newColDef(SWT.LEFT, Messages.PrescriptionTable_cuuong, 20);
		newColDef(SWT.LEFT, Messages.PrescriptionTable_luuy, 40);
	}
	
	Table getTable() {
		return this.getTableViewer().getTable();
	}

	@SuppressWarnings("unchecked")
	List<PrescriptionItem> getInput() {
		return (List<PrescriptionItem>) this.getTableViewer().getInput();
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
			//case 0: return model.getIndex() + ") " + model.getDrug(); // TODO for debug only
			case 0: return model.getDrug();
			case 1: return model.getDrugDesc();
			case 2: return PrescriptionView.DF.format(model.getNumDay());
			case 3: return model.getUnit();
			case 4: return PrescriptionView.DF.format(model.getPerSs());
			case 5: return model.getUse();
			case 6: return model.getSs();
			case 7: return model.getNote();
		}
		return ""; //$NON-NLS-1$
	}

	void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.getTableViewer().addSelectionChangedListener(listener);
	}

	private static final Comparator<PrescriptionItem> sortByIndex = new Comparator<PrescriptionItem>() {
		
		@Override
		public int compare(PrescriptionItem p1, PrescriptionItem p2) {
			if (p1 == null || p2 == null) {
				return 0;
			}
			return Integer.valueOf(p1.getIndex()).compareTo(p2.getIndex());
		}
	};
	
	void setInput(List<PrescriptionItem> data) {
		setInput(data, false);
	}
	
	private void setInput(List<PrescriptionItem> data, boolean updateIndex) {
		if (updateIndex) {
			updateIndex(data);
		}
		if (data != null) {
			Collections.sort(data, sortByIndex);
		}
		this.getTableViewer().setInput(data);
	}

	void updateLine(PrescriptionItem currentItem) {
		@SuppressWarnings("unchecked")
		List<PrescriptionItem> data = (List<PrescriptionItem>) this.getTableViewer().getInput();
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
		setInput(data, true);
	}

	void delete(String drug) {
		@SuppressWarnings("unchecked")
		List<PrescriptionItem> data = (List<PrescriptionItem>) this.getTableViewer().getInput();
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
		setInput(data, true);
	}

	void downLine(String drugName) {
		upLine(drugName, false);
	}

	void upLine(String drugName) {
		upLine(drugName, true);
	}

	@SuppressWarnings("unchecked")
	private void upLine(String drugName, boolean isUp) {
		List<PrescriptionItem> data = (List<PrescriptionItem>) this.getTableViewer().getInput();
		if (data == null || data.isEmpty()) {
			return;
		}
		
		int oldIndex = 0;
		PrescriptionItem itemToUpdate = null;
		for (PrescriptionItem item : data) {
			if (item.getDrug().equalsIgnoreCase(drugName)) {
				itemToUpdate = item;
				break;
			}
			oldIndex++;
		}
		if (oldIndex > data.size() - 1) {
			return;
		}
		
		int newIndex = -1;
		if (isUp) {
			newIndex = oldIndex - 1;
			if (newIndex < 0) {
				newIndex = 0;
			}
		} else {
			newIndex = oldIndex + 1;
			if (newIndex > data.size() - 1) {
				newIndex = data.size() - 1;
			}
		}
		PrescriptionItem itemAtNewIndex = data.get(newIndex);
		data.set(newIndex, itemToUpdate);
		data.set(oldIndex, itemAtNewIndex);
		
		setInput(data, true);
	}

	private void updateIndex(List<PrescriptionItem> data) {
		if (data == null || data.isEmpty()) {
			return;
		}
		// standardize index
		for (int i = 0; i < data.size(); i++) {
			PrescriptionItem item = data.get(i);
			item.setIndex(i);
		}
	}
}