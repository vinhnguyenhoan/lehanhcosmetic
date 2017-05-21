package com.lehanh.pama.ui.patientcase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Text;

import com.lehanh.pama.ICatagoryManager;
import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.CatagoryType;
import com.lehanh.pama.catagory.NonIdentifiedSurgeryCatalog;
import com.lehanh.pama.ui.util.ACommonCombositeDataProvider;

class PatientCaseCatagoryDataProvider extends ACommonCombositeDataProvider {

	private final CatagoryType type;
	private TableComboViewer tableComboViewer;
	private ICatagoryManager catManager;
	
	private TreeMap<String, Catagory> multiSelectionCatList = new TreeMap<String, Catagory>();
	private TreeMap<String, Catagory> input = new TreeMap<String, Catagory>();
	
	private List<PatientCaseCatagoryDataProvider> dependViewers;
	private Color backgroundSelected;
	private Text otherText;
	private Comparator<Catagory> catagoryComparator = new Comparator<Catagory>() {
		
		@Override
		public int compare(Catagory o1, Catagory o2) {
			if (o1 == null || o2 == null || o1.getDesc() == null || o2.getDesc() == null) {
				return 0;
			}
			return o1.getDesc().compareTo(o2.getDesc());
		}
	};
	
	PatientCaseCatagoryDataProvider(ICatagoryManager catManager, Color backgroundSelected, final TableComboViewer tableComboViewer, CatagoryType type, 
			PatientCaseCatagoryDataProvider... dependViewers) {
		this(catManager, false, backgroundSelected, tableComboViewer, type, dependViewers);
	}
	
	PatientCaseCatagoryDataProvider(ICatagoryManager catManager, boolean showDataAtFirst, Color backgroundSelected, final TableComboViewer tableComboViewer, CatagoryType type, 
			PatientCaseCatagoryDataProvider... dependViewers) {
		this(catManager, showDataAtFirst, backgroundSelected, tableComboViewer, type, 
				null, dependViewers);
	}

	PatientCaseCatagoryDataProvider(ICatagoryManager catManager, Color backgroundSelected, final TableComboViewer tableComboViewer, CatagoryType type, 
			Text otherText, PatientCaseCatagoryDataProvider... dependViewers) {
		this(catManager, false, backgroundSelected, tableComboViewer, type, otherText, dependViewers);
	}
	
	private PatientCaseCatagoryDataProvider(ICatagoryManager catManager, boolean showDataAtFirst, Color backgroundSelected, final TableComboViewer tableComboViewer, CatagoryType type, 
			Text otherText, PatientCaseCatagoryDataProvider... dependViewers) {
		this.catManager = catManager;
		this.backgroundSelected = backgroundSelected;
		this.tableComboViewer = tableComboViewer;
		this.type = type;
		this.dependViewers = Arrays.asList(dependViewers);
		this.otherText = otherText;
		
		if (otherText != null) {
			otherText.addModifyListener(this);
		}
		
		this.tableComboViewer.setContentProvider(this);
		// set the label providers
		this.tableComboViewer.setLabelProvider(this);
		if (showDataAtFirst) {
			// load the data
			this.tableComboViewer.setInput(catManager.getCatagoryByType(type).values());
		}
		// add listener
		this.tableComboViewer.addSelectionChangedListener(this);
		
		this.tableComboViewer.getTableCombo().setShowTableHeader(false);
		this.tableComboViewer.getTableCombo().defineColumns(new String[] { "Description" });
		this.tableComboViewer.getTableCombo().setClosePopupAfterSelection(false);
		this.tableComboViewer.getTableCombo().getTextControl().addModifyListener(this);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		Catagory model = (Catagory) ((IStructuredSelection) event.getSelection()).getFirstElement();
		if (model == null) {
			return;
		}
		selectionChanged(Arrays.asList(model.getName()), false);
	}
	
	void selectionChanged(List<String> catNameList) {
		selectionChanged(catNameList, true);
	}
	
	private void selectionChanged(List<String> catNameList, boolean firstSelection) {
		if (firstSelection) {
			multiSelectionCatList.clear();
			tableComboViewer.refresh();
		}
		if (catNameList == null || input == null || input.isEmpty()) {
			return;
		}
		long nonIdentifiedCatId = -1;
		for (String catName : catNameList) {
			Catagory cat = input.get(catName);// getCatByName(multiSelectionCatList, catName);
			if (cat == null) {
				// Allow to show a non-identified catalog
				cat = new NonIdentifiedSurgeryCatalog(nonIdentifiedCatId--, catName);
			}
			if (multiSelectionCatList.containsKey(catName)) {
				multiSelectionCatList.remove(catName);
			} else {
				multiSelectionCatList.put(catName, cat);
			}
			tableComboViewer.update(cat, null);
		}
		String selectionText = getSelectionText(multiSelectionCatList);
		tableComboViewer.getTableCombo().setText(selectionText);
		
		notifyDependViewers(dependViewers, multiSelectionCatList, isContainOtherText());
	}
	
	List<? extends Catagory> getMultiSelectionCatList() {
		return new LinkedList<Catagory>(this.multiSelectionCatList.values());
	}
	
	private static String getSelectionText(TreeMap<String, Catagory> multiSelectionCatList) {
		if (multiSelectionCatList == null) {
			return StringUtils.EMPTY;
		}
		Iterator<Catagory> it = multiSelectionCatList.values().iterator();
		String selectionText = "";
		if (it.hasNext()) {
			selectionText += it.next().getDesc();
		}
		while (it.hasNext()) {
			selectionText += ", " + it.next().getDesc();
		}
		return selectionText;
	}
	
	private void notifyDependViewers(List<PatientCaseCatagoryDataProvider> dependViewers, TreeMap<String, Catagory> multiSelectionCatList, boolean containOtherText) {
		if (dependViewers == null) {
			return;
		}
		for (PatientCaseCatagoryDataProvider dependViewer : dependViewers) {
			dependViewer.changedByParent(this.type, multiSelectionCatList == null ? null : multiSelectionCatList.values(), containOtherText);
		}
	}

	private void changedByParent(CatagoryType typeChanged, Collection<Catagory> multiSelectionCatList, boolean containOtherText) {
		if (!containOtherText && (multiSelectionCatList == null || multiSelectionCatList.isEmpty())) {
			tableComboViewer.setInput(null);
			return;
		}
		
		Collection<Catagory> newInputC = null;
		if (multiSelectionCatList != null && !multiSelectionCatList.isEmpty()) {
			TreeMap<Long, Catagory> result = new TreeMap<Long, Catagory>();
			for (Catagory catSelected : multiSelectionCatList) {
				List<Catagory> filterdCats = catManager.getSubCatagorysByParent(catSelected.getType(), catSelected.getId(), this.type);
				for (Catagory filterdCat : filterdCats) {
					result.put(filterdCat.getId(), filterdCat);
				}
			}
			newInputC = result.values();
		} else if (containOtherText) {
			newInputC = catManager.getCatagoryByType(this.type).values();
		}
		
		List<Catagory> newInput = null;
		if (newInputC != null) {
			newInput = new LinkedList<>(newInputC);
			Collections.sort(newInput, this.catagoryComparator );
		}
		tableComboViewer.setInput(newInput);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		((TableComboViewer) viewer).getTableCombo().setText(StringUtils.EMPTY);
		//((TableComboViewer) viewer).setInput(((TableComboViewer) viewer).getInput());
		multiSelectionCatList.clear();
		
		this.input = new TreeMap<String, Catagory>();
		if (newInput != null) {
			@SuppressWarnings({ "unchecked" })
			Collection<Catagory> newCatList = (Collection<Catagory>) newInput;
			for (Catagory cat : newCatList) {
				this.input.put(cat.getName(), cat);
			}
		}
		
		notifyDependViewers(dependViewers, multiSelectionCatList, isContainOtherText());
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Object[]) {
			return (Object[]) inputElement;
		}
        if (inputElement instanceof Collection) {
			return ((Collection<?>) inputElement).toArray();
		}
        return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		Catagory model = (Catagory) element;
		switch (columnIndex) {
			case 0: return model.getDesc();
		}
		return "";
	}

	/* (non-Javadoc) 
	 * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
	 */
	public Color getBackground(Object element, int columnIndex) {
		if (isSelected(element)) {
			return backgroundSelected;
		}
		return null;
	}
	
	private boolean isSelected(Object element) {
		if (element == null) {
			return false;
		}
		
		return multiSelectionCatList.containsKey(((Catagory) element).getName());
	}

	private String currOtherTextValue;
	@Override
	public void modifyText(ModifyEvent e) {
		if (otherText != null && e.getSource() == otherText) {
			final String newOtherText = otherText.getText();
			boolean isOldTextPlan = StringUtils.isBlank(currOtherTextValue); 
			boolean isNewTextPlan = StringUtils.isBlank(newOtherText);
			
			if (isOldTextPlan == isNewTextPlan) {
				return;
			}
			notifyDependViewers(dependViewers, multiSelectionCatList, isContainOtherText());
			this.currOtherTextValue = otherText.getText();
		} else {
			String text = tableComboViewer.getTableCombo().getText();
			if (StringUtils.isBlank(text)) {
				tableComboViewer.refresh();
				multiSelectionCatList.clear();
				notifyDependViewers(dependViewers, multiSelectionCatList, isContainOtherText());
			}
		}
	}

	private boolean isContainOtherText() {
		return otherText != null && !otherText.getText().isEmpty();
	}
}
