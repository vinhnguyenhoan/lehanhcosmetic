package com.lehanh.pama.ui;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;

/**
 * This is a stub implementation containing e4 LifeCycle annotated methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the
 * <em>org.eclipse.core.runtime.products' extension point</em>) that references
 * this class.
 **/
@SuppressWarnings("restriction")
public class E4LifeCycle {

	public static IEclipseContext workbenchContext;

	@PostContextCreate
	void postContextCreate(IEclipseContext workbenchContext) {
		E4LifeCycle.workbenchContext = workbenchContext;
		System.out.println(workbenchContext);
	}

	@PreSave
	void preSave(IEclipseContext workbenchContext) {
		System.out.println(workbenchContext);
	}

	@ProcessAdditions
	void processAdditions(IEclipseContext workbenchContext) {
		System.out.println(workbenchContext);
	}

	@ProcessRemovals
	void processRemovals(IEclipseContext workbenchContext) {
		System.out.println(workbenchContext);
	}
}
