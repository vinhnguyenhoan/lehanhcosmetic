package com.lehanh.pama.old.model.v3;

import java.util.List;

import com.lehanh.pama.old.model.BenhNhan;

public interface LoadPatientListenner {

	void afterLoadedOldBN(List<BenhNhan> listBN);

	void beforeLoadedOldBN();
}
