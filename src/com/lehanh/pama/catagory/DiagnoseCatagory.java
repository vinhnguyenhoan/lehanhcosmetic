package com.lehanh.pama.catagory;

public class DiagnoseCatagory extends Catagory {

	private static final long serialVersionUID = -6722427734559723915L;

	public DiagnoseCatagory() {
		super(CatagoryType.DIAGNOSE);
	}

	DiagnoseCatagory(Long id, CatagoryType catagoryType) {
		super(id, catagoryType);
	}

}
