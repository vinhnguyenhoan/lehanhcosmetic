package com.lehanh.pama.ui.util;

import com.lehanh.pama.catagory.Catagory;

@SuppressWarnings("rawtypes")
public class CatagoryToUITextByDesc implements ObjectToUIText {

	@Override
	public String showUI(Object object) {
		return ((Catagory) object).getDesc();
	}

	@Override
	public Object getIdForUI(Object object) {
		return ((Catagory) object).getId();
	}

}
