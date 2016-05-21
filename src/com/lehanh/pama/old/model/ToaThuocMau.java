package com.lehanh.pama.old.model;

import java.util.ArrayList;
import java.util.List;

public class ToaThuocMau {
	public String ten;
	public List<ChiTietToaThuoc> dsChiTiet = new ArrayList<ChiTietToaThuoc>();
	public ToaThuocMau(String ten) {
		super();
		this.ten = ten;
	}
}
