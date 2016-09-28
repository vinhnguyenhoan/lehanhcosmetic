package com.lehanh.pama.catagory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import com.google.gson.annotations.Expose;
import com.lehanh.pama.IJsonDataObject;
import com.lehanh.pama.util.JsonMapper;

public class DrugCatagory extends Catagory implements IContainJsonDataCatagory, IJsonDataObject, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4931505865789613635L;
	
	@Expose
	private String use;
	@Expose
	private float total;
	@Expose
	private String unit;
	@Expose
	private String unitPer;
	@Expose
	private float numDay;
	@Expose
	private float perDay;
	@Expose
	private int numSs;
	@Expose
	private float perSs;
	@Expose
	private String ss;
	@Expose
	private String note;
	@Expose
	private String drugDesc;
	
	public DrugCatagory() {
		super(CatagoryType.DRUG);
	}

	public DrugCatagory(String name, String desc, String use, float total, String unit, String unitPer, float numDay, float perDay, 
			int numSs, float perSs, String ss, String note) {
		this();
		this.setName(name);
		this.setDesc(desc);
		updateData(use, total, unit, unitPer, numDay, perDay, numSs, perSs, ss, note);
	}
	
	public void updateData(String use, float total, String unit, String unitPer, float numDay, float perDay, 
			int numSs, float perSs, String ss, String note) {
		this.use = use;
		this.unit = unit;
		this.unitPer = unitPer;
		this.numDay = numDay;
		this.perDay = perDay;
		this.numSs = numSs;
		this.perSs = perSs;
		this.ss = ss;
		this.note = note;
	}
	
	DrugCatagory(Long id, CatagoryType catagoryType) {
		super(id, catagoryType);
	}

	public DrugCatagory(String cachSuDung, String cu, String donVi, String donViSuDung, String luuY,
			String ma, int soLanSuDungTrenNgay, int soLuong, String soLuongSuDungTrenLanText, String ten) {
		super(CatagoryType.DRUG);
		this.use = cachSuDung;
		this.ss = cu;
		this.unit = donVi;
		this.unitPer = donViSuDung;
		this.note = luuY;
		this.setName(ten);
		this.drugDesc = ma;

		this.numSs = soLanSuDungTrenNgay;
		this.total = soLuong;
		if (soLuongSuDungTrenLanText != null) {
			if (soLuongSuDungTrenLanText.trim().equalsIgnoreCase("1/2")) {
				this.perSs = 0.5f;
			} else {
				this.perSs = Integer.parseInt(soLuongSuDungTrenLanText);
			}
		}
		if (perSs > 0) {
			this.perDay = numSs * perSs;
		}
		this.numDay = this.total / this.perDay;
	}
	
	public DrugCatagory(CatagoryType catagoryType, String name, String symbol, String desc, Long oldId) {
		super(catagoryType, name, symbol, desc, oldId);
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitPer() {
		return unitPer;
	}

	public void setUnitPer(String unitPer) {
		this.unitPer = unitPer;
	}

	public float getNumDay() {
		return numDay;
	}

	public void setNumDay(float numDay) {
		this.numDay = numDay;
	}

	public float getPerDay() {
		return perDay;
	}

	public void setPerDay(float perDay) {
		this.perDay = perDay;
	}

	public int getNumSs() {
		return numSs;
	}

	public void setNumSs(int numSs) {
		this.numSs = numSs;
	}

	public float getPerSs() {
		return perSs;
	}

	public void setPerSs(float perSs) {
		this.perSs = perSs;
	}

	public String getSs() {
		return ss;
	}

	public void setSs(String ss) {
		this.ss = ss;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDrugDesc() {
		return drugDesc;
	}

	public void setDrugDesc(String drugDesc) {
		this.drugDesc = drugDesc;
	}

	@Override
	public void updateFromText() {
		DrugCatagory otherData = JsonMapper.fromJson(getOtherDataText(), DrugCatagory.class);
		if (otherData == null) {
			return;
		}
		updateData(otherData.use, otherData.total, otherData.unit, otherData.unitPer, otherData.numDay, otherData.perDay, 
				otherData.numSs, otherData.perSs, otherData.ss, otherData.note);
		this.drugDesc = otherData.drugDesc;
	}

	@Override
	public String toOtherDataAsText() {
		return JsonMapper.toJson(this);
	}
	
	public String getTotalNote() {
		float totalUnit = this.numDay * this.numSs * this.perSs;
		return String.valueOf(totalUnit) + " " + unit; //$NON-NLS-1$
	}

	private static final TreeMap<Integer, String[]> sessionPerDay = new TreeMap<Integer, String[]>();
	static {
		sessionPerDay.put(1, new String[]{Messages.DrugCatagory_sang, Messages.DrugCatagory_trua, Messages.DrugCatagory_chieu, Messages.DrugCatagory_toi});
		sessionPerDay.put(2, new String[]{Messages.DrugCatagory_sangtrua, Messages.DrugCatagory_sangchieu, Messages.DrugCatagory_sangtoi, Messages.DrugCatagory_truachieu, Messages.DrugCatagory_truatoi, Messages.DrugCatagory_chieutoi});
		sessionPerDay.put(3, new String[]{Messages.DrugCatagory_sangtruachieu, Messages.DrugCatagory_sangtruatoi, Messages.DrugCatagory_sangchieutoi, Messages.DrugCatagory_truachieutoi});
		sessionPerDay.put(4, new String[]{Messages.DrugCatagory_sangtruachieutoi});
	}
	
	public static List<String> generateSessionPerDay(int perDay) {
		String[] ssPDAsArr = sessionPerDay.get(perDay);
		if (ssPDAsArr == null) {
			return new LinkedList<String>();
		}
		return Arrays.asList(ssPDAsArr);
	}

}
