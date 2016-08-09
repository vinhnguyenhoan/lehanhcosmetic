package com.lehanh.pama.old.model.v3;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.lehanh.pama.old.model.BenhNhan;
import com.lehanh.pama.old.model.LanKhamBenh;

public class StatictisBN {
	
	BenhNhan bn;
	List<LanKhamBenh> lkb = new LinkedList<>();
	
	static final Comparator<LanKhamBenh> comparatorLKBByDate = new Comparator<LanKhamBenh>() {

		@Override
		public int compare(LanKhamBenh o1, LanKhamBenh o2) {
			return o2.ngayKham.compareTo(o1.ngayKham);
		}
	};
	
	StatictisBN(BenhNhan bn) {
		this.bn = bn;
	}

	void sortLKB() {
		
		Collections.sort(lkb, comparatorLKBByDate);
	}
}
