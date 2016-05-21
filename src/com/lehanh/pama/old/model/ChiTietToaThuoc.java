package com.lehanh.pama.old.model;

public class ChiTietToaThuoc extends DanhMuc {
	public String ma;
	public int soLuong;
	public String donVi;
	public int soLanSuDungTrenNgay;
	public String soLuongSuDungTrenLan;
	public String donViSuDung;
	public String cu;
	public String cachSuDung;
	public String luuY;
	public ChiTietToaThuoc(long id, String ten, String ma, int soLuong, String donVi, int soLanSuDungTrenNgay, String soLuongSuDungTrenLan,
			String donViSuDung, String cu, String cachSuDung, String luuY) {
		super(id, ten);
		this.ma = ma;
		this.soLuong = soLuong;
		this.donVi = donVi;
		this.soLanSuDungTrenNgay = soLanSuDungTrenNgay;
		this.soLuongSuDungTrenLan = soLuongSuDungTrenLan;
		this.donViSuDung = donViSuDung;
		this.cu = cu;
		this.cachSuDung = cachSuDung;
		this.luuY = luuY;
	}
	public ChiTietToaThuoc(String string, String string2, String string3, String string4, String string5,
			String string6, int parseInt, int parseInt2, String string7, String string8) {
		// TODO Auto-generated constructor stub
	}
}
