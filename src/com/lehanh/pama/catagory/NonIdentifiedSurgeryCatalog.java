package com.lehanh.pama.catagory;

public class NonIdentifiedSurgeryCatalog extends SurgeryCatagory {

	public NonIdentifiedSurgeryCatalog(CatagoryType type) {
		super(type);
	}

	public NonIdentifiedSurgeryCatalog(long id, String catName) {
		super(CatagoryType.NON_IDENTIFIED, catName, catName);
	}

}
