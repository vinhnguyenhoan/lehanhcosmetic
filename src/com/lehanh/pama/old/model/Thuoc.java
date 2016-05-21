package com.lehanh.pama.old.model;

public class Thuoc extends DanhMuc {
	public String ma;

	public Thuoc(long id, String ma, String ten) {
		super(id, ten);
		this.ma = ma;
	}
}
