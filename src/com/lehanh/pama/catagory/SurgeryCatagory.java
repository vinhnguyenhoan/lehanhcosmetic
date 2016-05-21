package com.lehanh.pama.catagory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lehanh.pama.IJsonDataObject;
import com.lehanh.pama.util.JsonMapper;

public class SurgeryCatagory extends Catagory implements IContainJsonDataCatagory, IJsonDataObject {

	@Expose
	@SerializedName("advice")
	private String advice;
	
	public SurgeryCatagory() {
		super(CatagoryType.SURGERY);
	}

	SurgeryCatagory(Long id, CatagoryType catagoryType) {
		super(id, CatagoryType.SURGERY);
	}

	@Override
	public void updateFromText() {
		SurgeryCatagory otherData = JsonMapper.fromJson(getOtherDataText(), SurgeryCatagory.class);
		if (otherData == null) {
			return;
		}
		advice = otherData.advice;
	}

	@Override
	public String toOtherDataAsText() {
		return JsonMapper.toJson(this);
	}

	public String getAdvice() {
		return advice;
	}
}