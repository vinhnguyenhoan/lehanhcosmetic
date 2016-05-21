package com.lehanh.pama.catagory;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.lehanh.pama.IJsonDataObject;
import com.lehanh.pama.util.JsonMapper;

public class PrescriptionCatagory extends Catagory implements IContainJsonDataCatagory, IJsonDataObject, Serializable {

	private static final long serialVersionUID = -7273917310053601309L;

	// private static final String USE_F = "use";
	// private static final String DRUG_NAME_F = "drug";
	// private static final String TOTAL_F = "total";
	// private static final String UNIT_F = "unit";
	// private static final String TOTAL_DAY_F = "day";
	// private static final String NUMBER_PER_SESSION_F = "perSs";
	// private static final String SESSION_F = "ss";
	// private static final String NOTICE_F = "note";
	
	@Expose
	//@SerializedName("data")
	//private TreeMap<Long, TreeMap<String, Object>> rawData;
	private List<PrescriptionItem> data;
	
	public PrescriptionCatagory() {
		super(CatagoryType.PRESCRIPTION);
	}
	
	PrescriptionCatagory(Long id, CatagoryType type) {
		super(id, type);
	}

	public List<PrescriptionItem> getData() {
		return data;
	}

	public void setData(List<PrescriptionItem> data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see com.lehanh.pama.catagory.IContainJsonDataCatagory#updateFromText()
	 */
	@Override
	public void updateFromText() {
		PrescriptionCatagory otherData = JsonMapper.fromJson(getOtherDataText(), PrescriptionCatagory.class);
		if (otherData == null) {
			return;
		}
		this.data = otherData.data;
	}

	/* (non-Javadoc)
	 * @see com.lehanh.pama.catagory.IContainJsonDataCatagory#toOtherDataAsText()
	 */
	@Override
	public String toOtherDataAsText() {
		return JsonMapper.toJson(this);
	}

}
