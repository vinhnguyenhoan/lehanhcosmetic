package com.lehanh.pama.catagory;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.lehanh.pama.IJsonDataObject;

public class PrescriptionItem implements IJsonDataObject, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3576622959257845837L;

	@Expose
	private String use;
	@Expose
	private String drug;
	@Expose
	private String otherDrug;
	@Expose
	private String drugDesc;
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
	
	public PrescriptionItem() {
	}

	//ct.cachSuDung, ct.cu, ct.donVi, ct.donViSuDung, ct.luuY, ct.ma, ""+ct.soLanSuDungTrenNgay, ""+ct.soLuong, ct.soLuongSuDungTrenLan, ct.ten
	public PrescriptionItem(String cachSuDung, String cu, String donVi, String donViSuDung, String luuY,
			String ma, int soLanSuDungTrenNgay, int soLuong, String soLuongSuDungTrenLanText, String ten) {
		this.use = cachSuDung;
		this.ss = cu;
		this.unit = donVi;
		this.unitPer = donViSuDung;
		this.note = luuY;
		// TODO this.drugDesc = ma;
		this.drug = ten;
		
		this.numSs = soLanSuDungTrenNgay;
		this.total = soLuong;
		if (soLuongSuDungTrenLanText != null) {
			soLuongSuDungTrenLanText = soLuongSuDungTrenLanText.trim();
			if (soLuongSuDungTrenLanText.contains("/")) {
				String[] s = soLuongSuDungTrenLanText.split("/");
				this.perSs = Float.parseFloat(s[0]) / Float.parseFloat(s[1]);
			} else {
				this.perSs = Integer.parseInt(soLuongSuDungTrenLanText);
			}
		}
		if (perSs > 0) {
			this.perDay = numSs * perSs;
		}
		this.numDay = this.total / this.perDay;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	public String getDrugDesc() {
		return drugDesc;
	}

	public void setDrugDesc(String drugDesc) {
		this.drugDesc = drugDesc;
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

	public String getOtherDrug() {
		return this.otherDrug;
	}

	public void setOtherDrug(String otherDrug) {
		this.otherDrug = otherDrug;
	}

	public String getTotalNote() {
		float totalUnit = this.numDay * this.numSs * this.perSs;
		return String.valueOf(totalUnit) + " " + unit; //$NON-NLS-1$
	}

}
