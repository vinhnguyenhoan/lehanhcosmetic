package com.lehanh.pama.catagory;

import java.util.LinkedList;
import java.util.List;
import static com.lehanh.pama.catagory.CatagoryType.*;

public class DrugInfo extends Catagory implements InternalCatagory {

	DrugInfo(Long id, CatagoryType catagoryType) {
		super(id, catagoryType);
	}

	@Override
	public List<Catagory> createCatagoryList() {
		List<Catagory> result = new LinkedList<Catagory>();
		long index = 1;
		if (DRUG_UNIT == this.getType()) {
			result.add(DRUG_UNIT.createCatalog(index++, Messages.DrugInfo_vien, Messages.DrugInfo_vien));
			result.add(DRUG_UNIT.createCatalog(index++, Messages.DrugInfo_ong, Messages.DrugInfo_ong));
			result.add(DRUG_UNIT.createCatalog(index++, Messages.DrugInfo_chai, Messages.DrugInfo_chai));
			result.add(DRUG_UNIT.createCatalog(index++, Messages.DrugInfo_goi, Messages.DrugInfo_goi));
			result.add(DRUG_UNIT.createCatalog(index++, Messages.DrugInfo_tuyp, Messages.DrugInfo_tuyp));
			return result;
		}

		if (DRUG_NOTICE == this.getType()) {
			result.add(DRUG_NOTICE.createCatalog(index++, Messages.DrugInfo_uongtruockhian, Messages.DrugInfo_uongtruockhian));
			result.add(DRUG_NOTICE.createCatalog(index++, Messages.DrugInfo_uongsaukhian, Messages.DrugInfo_uongsaukhian));
			result.add(DRUG_NOTICE.createCatalog(index++, Messages.DrugInfo_uongtruockhingu30phut, Messages.DrugInfo_uongtruockhingu30phut));
			result.add(DRUG_NOTICE.createCatalog(index++, Messages.DrugInfo_xitsaukhiruamui, Messages.DrugInfo_xitsaukhiruamui));
			result.add(DRUG_NOTICE.createCatalog(index++, Messages.DrugInfo_thoalenvetthuong, Messages.DrugInfo_thoalenvetthuong));
			result.add(DRUG_NOTICE.createCatalog(index++, Messages.DrugInfo_thoalenseo, Messages.DrugInfo_thoalenseo));
			return result;
		}

		if (DRUG_SESSION_PER_DAY == this.getType()) {
			result.add(DRUG_SESSION_PER_DAY.createCatalog(index++, Messages.DrugInfo_sang, Messages.DrugInfo_sang));
			result.add(DRUG_SESSION_PER_DAY.createCatalog(index++, Messages.DrugInfo_trua, Messages.DrugInfo_trua));
			result.add(DRUG_SESSION_PER_DAY.createCatalog(index++, Messages.DrugInfo_chieu, Messages.DrugInfo_chieu));
			result.add(DRUG_SESSION_PER_DAY.createCatalog(index++, Messages.DrugInfo_toi, Messages.DrugInfo_toi));
			return result;
		}

		if (DRUG_USE == this.getType()) {
			result.add(DRUG_USE.createCatalog(index++, Messages.DrugInfo_uong, Messages.DrugInfo_uong));
			result.add(DRUG_USE.createCatalog(index++, Messages.DrugInfo_xit, Messages.DrugInfo_xit));
			result.add(DRUG_USE.createCatalog(index++, Messages.DrugInfo_nhotai, Messages.DrugInfo_nhotai));
			result.add(DRUG_USE.createCatalog(index++, Messages.DrugInfo_boi, Messages.DrugInfo_boi));
			result.add(DRUG_USE.createCatalog(index++, Messages.DrugInfo_ruamui, Messages.DrugInfo_ruamui));
			result.add(DRUG_USE.createCatalog(index++, Messages.DrugInfo_suchong, Messages.DrugInfo_suchong));
			result.add(DRUG_USE.createCatalog(index++, Messages.DrugInfo_chich, Messages.DrugInfo_chich));
			return result;
		}

		return result;
	}

}