package com.lehanh.pama.old.model;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class LanKhamBenh {
	public long id;
	public String tienCan;
	public boolean caoHuyetAp;
	public boolean tangDuongHuyet;
	public boolean viemGan;
	public String benhSu;
	public String dienBienBenh;
	public String mach;
	public String huyetAp;
	public String nhietDo;
	public String canNang;
	public BacSy bacSyKham;
	public List<DanhMuc> danhSachTrieuChung = new ArrayList<DanhMuc>();
	public String trieuCungNgoaiDS;
	public List<DanhMuc> danhSachChuanDoan = new ArrayList<DanhMuc>();
	public String chuanDoanNgoaiDS;
	public List<PhauThuat> danhSachPhauThuat = new ArrayList<PhauThuat>();
	public Date ngayPhauThuat;
	public String thuThuat;
	public int lanKham = 1;
	public int lanTaiKham = 0;
	public Date ngayKham;
	public Date ngayTaiKham;
	public String loiKhuyen;
	public List<ChiTietToaThuoc> danhSachChiTietToaThuoc = new ArrayList<ChiTietToaThuoc>();
	public List<HinhAnh> danhSachHinhAnh = new ArrayList<HinhAnh>();
	public LanKhamBenh(long id, String tienCan, boolean caoHuyetAp, boolean tangDuongHuyet, boolean viemGan, String benhSu,
			String dienBienBenh, String mach, String huyetAp, String nhietDo, String canNang, BacSy bacSyKham, String trieuCungNgoaiDS,
			String chuanDoanNgoaiDS, String thuThuat, int lanKham, int lanTaiKham, Date ngayKham, Date ngayTaiKham, String loiKhuyen,Date ngayPhauThuat) {
		super();
		this.id = id;
		this.tienCan = tienCan;
		this.caoHuyetAp = caoHuyetAp;
		this.tangDuongHuyet = tangDuongHuyet;
		this.viemGan = viemGan;
		this.benhSu = benhSu;
		this.dienBienBenh = dienBienBenh;
		this.mach = mach;
		this.huyetAp = huyetAp;
		this.nhietDo = nhietDo;
		this.canNang = canNang;
		this.bacSyKham = bacSyKham;
		this.trieuCungNgoaiDS = trieuCungNgoaiDS;
		this.chuanDoanNgoaiDS = chuanDoanNgoaiDS;
		this.thuThuat = thuThuat;
		this.lanKham = lanKham;
		this.lanTaiKham = lanTaiKham;
		this.ngayKham = ngayKham;
		this.ngayTaiKham = ngayTaiKham;
		this.loiKhuyen = loiKhuyen;
		this.ngayPhauThuat = ngayPhauThuat;
	}
}
