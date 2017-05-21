package com.lehanh.pama.ui.util;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public abstract class TableDataProvider extends ACommonCombositeDataProvider {

	private final TableViewer tableViewer;
	private final TableColumnLayout layout;
	
	protected TableDataProvider(Composite composite_1) {
		this(composite_1, null);
	}
	
	protected TableDataProvider(Composite composite_1, Object comLayout) {
		Composite tableComp = new Composite(composite_1, SWT.NONE);
		if (comLayout == null) {
			comLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		}
		tableComp.setLayoutData(comLayout);
		this.tableViewer = new TableViewer(tableComp, SWT.H_SCROLL
		        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		this.layout = new TableColumnLayout();
		tableComp.setLayout(layout);

		this.tableViewer.setContentProvider(this);
		// set the label providers
		this.tableViewer.setLabelProvider(this);
		//pack();

	}
	
	protected void newColDef(int style, String text, int width) {
		TableColumn sttCol = new TableColumn(tableViewer.getTable(), style);
		sttCol.setText(text);
		//sttCol.setWidth(width);
		layout.setColumnData(sttCol, new ColumnWeightData(width));
	}
	
	protected TableViewer getTableViewer() {
		return this.tableViewer;
	}
	
	protected void pack() {
		for (TableColumn col : this.tableViewer.getTable().getColumns()) {
			col.pack();
		}
	}
}
