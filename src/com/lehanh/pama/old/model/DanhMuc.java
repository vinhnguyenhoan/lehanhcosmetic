package com.lehanh.pama.old.model;

public class DanhMuc {
	public long id;
	public String ten;
	public String sym;
	public DanhMuc(long id, String ten) {
		super();
		this.id = id;
		this.ten = ten;
	}
	public DanhMuc(long id, String ten, String sym) {
		super();
		this.id = id;
		this.ten = ten;
		this.sym = sym;
	}
	public DanhMuc() {
		super();
	}
}
