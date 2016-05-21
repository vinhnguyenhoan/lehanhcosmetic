package com.lehanh.pama.catagory;

import java.util.LinkedList;
import java.util.List;

public class AppointmentCatagory extends Catagory implements InternalCatagory {

	public AppointmentCatagory() {
		super(CatagoryType.APPOINTMENT);
	}

	AppointmentCatagory(Long id, CatagoryType catagoryType) {
		super(id, catagoryType);
	}

	@Override
	public List<Catagory> createCatagoryList() {
		List<Catagory> result = new LinkedList<Catagory>();
		long index = 1;
		result.add(this.getType().createCatalog(index++, "consultant_appointments", "H\u1EB9n t\u01B0 v\u1EA5n")); // Hẹn tư vấn //$NON-NLS-1$ //$NON-NLS-2$
		result.add(this.getType().createCatalog(index++, "reexam_after_surgery", "T\u00E1i kh\u00E1m sau ph\u1EA9u thu\u1EADt")); // Tái khám sau phẩu thuật //$NON-NLS-1$ //$NON-NLS-2$
		
		return result;
	}


}