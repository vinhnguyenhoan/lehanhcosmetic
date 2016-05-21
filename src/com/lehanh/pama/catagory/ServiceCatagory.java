package com.lehanh.pama.catagory;

public class ServiceCatagory extends Catagory {

	//private static final long serialVersionUID = 1164478787375164065L;

	public ServiceCatagory() {
		super(CatagoryType.SERVICE);
	}

	ServiceCatagory(Long id, CatagoryType catagoryType) {
		super(id, catagoryType);
	}

}