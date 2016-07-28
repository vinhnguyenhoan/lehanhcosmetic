package com.lehanh.pama.ui.patientcase;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.wb.swt.SWTResourceManager;

import com.lehanh.pama.patientcase.IPatientCaseList;
import com.lehanh.pama.patientcase.PatientCaseEntity;
import com.lehanh.pama.ui.util.ACommonComboViewer;

class ExamVersionComboViewer extends ACommonComboViewer {

	private final TableComboViewer tableComboViewer;
	private PatientCaseEntity selectedEntity;
	private IPatientCaseList patientCaseList;
	
	private ExamVersionComboViewer detailViewer;
//	private final TableComboViewer detailCaseTableComboViewer;
//	private PatientCaseEntity detailSelectedEntity;
//	private IPatientCaseList detailPatientCaseList;
	
	private final Color backgroundSelected;

	private final boolean isRoot;
	
	interface ISelectionDetailChangedListener {
		
		void viewDetailCase(PatientCaseEntity model);
	}
	
	private ISelectionDetailChangedListener selectionDetailListener;
	
	ExamVersionComboViewer(TableComboViewer rootCasetableComboViewer, TableComboViewer examVersionTComboViewer, 
			Color backgroundSelected) {
		this(rootCasetableComboViewer, backgroundSelected, true);
		this.detailViewer = new ExamVersionComboViewer(examVersionTComboViewer, backgroundSelected, false);
	}
	
	private ExamVersionComboViewer(TableComboViewer examVersionTComboViewer, Color backgroundSelected, boolean isRoot) {
		this.tableComboViewer = examVersionTComboViewer;
		this.backgroundSelected = backgroundSelected;
		this.isRoot = isRoot;
		initialTableCombo(this.tableComboViewer, true);
	}

	private void initialTableCombo(TableComboViewer tableComboViewer, boolean isRoot) {
		tableComboViewer.setContentProvider(this);
		// set the label providers
		tableComboViewer.setLabelProvider(this);
		// add listener
		tableComboViewer.addSelectionChangedListener(this);
		tableComboViewer.getTableCombo().getTextControl().addModifyListener(this);
		
		tableComboViewer.getTableCombo().setShowTableHeader(true);
		if (isRoot) {
			tableComboViewer.getTableCombo().defineColumns(new String[] {Messages.ExamVersionComboViewer_thutu, Messages.ExamVersionComboViewer_ngayden, Messages.ExamVersionComboViewer_phauthuat });
		} else {
			tableComboViewer.getTableCombo().defineColumns(new String[] {Messages.ExamVersionComboViewer_thutu, Messages.ExamVersionComboViewer_ngayden, Messages.ExamVersionComboViewer_thuchien });
		}
		tableComboViewer.getTableCombo().setClosePopupAfterSelection(true);
		tableComboViewer.getTableCombo().getTextControl().addModifyListener(this);
	}

	void addSelectionDetailChangedListener(ISelectionDetailChangedListener selectionDetailListener) {
		this.detailViewer.selectionDetailListener = selectionDetailListener;
	}
	
	TableCombo[] getTableCombos() {
		return new TableCombo[]{this.tableComboViewer.getTableCombo(), this.detailViewer.tableComboViewer.getTableCombo()};
	}

	IPatientCaseList getInput() {
		return this.patientCaseList;
	}
	
	IPatientCaseList getDetailInput() {
		if (this.selectedEntity == null) {
			return null;
		}
		return this.patientCaseList.getSubList(this.selectedEntity.getId());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		PatientCaseEntity model = (PatientCaseEntity) element;
		if (!isRoot) {
			switch (columnIndex) {
				case 0: return String.valueOf(model.getId());
				case 1: return model.getDateAsText();
				case 2: return model.getStatusEnum().desc;
			}
		} else {
			switch (columnIndex) {
				case 0: return String.valueOf(model.getId());
				case 1: return model.getDateAsText();
				case 2: {
					IPatientCaseList subList = patientCaseList.getSubList(model.getId());
					if (subList != null) {
						return subList.getSurgerySummary() == null ? StringUtils.EMPTY : subList.getSurgerySummary();
					}
				}
			}
		}
		return ""; //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see com.lehanh.pama.ui.util.ACommonComboViewer#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		PatientCaseEntity model = (PatientCaseEntity) ((IStructuredSelection) event.getSelection()).getFirstElement();
		if (model == null) {
			return;
		}
		
		selectionChanged(model);
	}

	private void selectionChanged(PatientCaseEntity model) {
		this.selectedEntity = model;
		String selectionText = getSelectionText(selectedEntity);
		this.tableComboViewer.getTableCombo().setText(selectionText);

		if (selectedEntity != null) {
			this.tableComboViewer.refresh();
			// notify detail viewer
			if (detailViewer != null) {
				this.detailViewer.setInput(patientCaseList.getSubList(selectedEntity.getId()));
			}
		}
		if (selectionDetailListener != null) {
			selectionDetailListener.viewDetailCase(selectedEntity);
		}
	}
	
	private String getSelectionText(PatientCaseEntity selectedEntity) {
		if (selectedEntity == null) {
			return StringUtils.EMPTY;
		}
		
		String lastExamText = StringUtils.EMPTY;
		if (this.patientCaseList.isLastCreatedExam(selectedEntity)) {
			lastExamText = Messages.ExamVersionComboViewer_langannhat;
		} else if (this.patientCaseList.isCreatingExam(selectedEntity)) {
			lastExamText = Messages.ExamVersionComboViewer_dangthuchien;
		}
		
		if (isRoot) {
			return selectedEntity.getDateAsText() + " - " + patientCaseList.getSubList(selectedEntity.getId()).getSurgerySummary() + lastExamText; //$NON-NLS-1$
		} else {
			return selectedEntity.getDateAsText() + " - " + selectedEntity.getStatusEnum().desc + lastExamText; //$NON-NLS-1$
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lehanh.pama.ui.util.ACommonComboViewer#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof IPatientCaseList) {
        	this.patientCaseList = (IPatientCaseList) newInput;
        	
    		// Default select last one and notify sub list combo
    		PatientCaseEntity lastRoot = patientCaseList.getLastExamHaveStatus();
    		this.selectionChanged(lastRoot);
        }
	}

	/*
	 * set input for root case, used from client of this class
	 */
	void setInput(IPatientCaseList patientCaseList) {
		this.tableComboViewer.setInput(patientCaseList);
	}
	
	/* (non-Javadoc)
	 * @see com.lehanh.pama.ui.util.ACommonComboViewer#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
        if (inputElement instanceof IPatientCaseList) {
        	Object[] allVers = ((IPatientCaseList) inputElement).getAllVersions();
        	return allVers;
		}
        
        return null;
	}
	
	/* (non-Javadoc) 
	 * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
	 */
	@Override
	public Color getBackground(Object element, int columnIndex) {
		if (isSelected(element)) {
			return backgroundSelected;
		}
		return null;
	}
	
	private boolean isSelected(Object element) {
		return selectedEntity != null && selectedEntity == element;
	}

	Integer getSelectedRootId() {
		if (selectedEntity == null) {
			return null;
		}
		return selectedEntity.getId();
	}
	
	PatientCaseEntity getSelectedDetailEntity() {
		return this.detailViewer.selectedEntity;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableFontProvider#getFont(java.lang.Object, int)
	 */
	@Override
	public Font getFont(Object element, int index) {
		PatientCaseEntity model = (PatientCaseEntity) element;
		if (model == null) {
			return null;
		}
		if (StringUtils.isBlank(model.getDateAsText())) {
			return SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		String text = tableComboViewer.getTableCombo().getText();
		if (StringUtils.isBlank(text)) {
			tableComboViewer.refresh();
			selectedEntity = null;
		}
	}

}