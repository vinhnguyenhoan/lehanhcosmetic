package com.lehanh.pama.old.model;

import java.sql.Date;

public class HinhAnh extends DanhMuc {
	public String thuMucAnh;
	public Date ngayCapNhat;
	public HinhAnh(long id, String ten, String thuMucAnh, Date ngayCapNhat) {
		super(id, ten);
		this.thuMucAnh = thuMucAnh;
		this.ngayCapNhat = ngayCapNhat;
	}
}
