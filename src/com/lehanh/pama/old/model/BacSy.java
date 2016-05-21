package com.lehanh.pama.old.model;


public class BacSy extends DanhMuc {
	public BacSy() {
		super();
	}
	public BacSy(long id, String ten) {
		super();
		this.id = id;
		this.ten = ten;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	
}
