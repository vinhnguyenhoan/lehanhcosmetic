package com.lehanh.pama.old.model;

public class Thuoc extends ChiTietToaThuoc {
	public String tenGoc;
	public String thongTinThem;
	public Thuoc(long id, String ten, String ma, int soLuong, String donVi, int soLanSuDungTrenNgay, String soLuongSuDungTrenLan,
			String donViSuDung, String cu, String cachSuDung, String luuY, String tenGoc, String thongTinThem) {
		super(id, ten, ma, soLuong, donVi, soLanSuDungTrenNgay, soLuongSuDungTrenLan, donViSuDung, cu, cachSuDung, luuY);
		this.tenGoc = tenGoc;
		this.thongTinThem = thongTinThem;
	}
}
