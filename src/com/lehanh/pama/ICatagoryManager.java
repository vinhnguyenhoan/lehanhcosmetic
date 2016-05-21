package com.lehanh.pama;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.CatagoryType;

public interface ICatagoryManager extends IService {
	
	Map<Long, Catagory> getCatagoryByType(CatagoryType catType);

	List<Catagory> getSubCatagorysByParent(CatagoryType parentType, Long parentId, CatagoryType type);
	
	void saveCatagory(Catagory cat) throws SQLException;

	Catagory getCatagoryByTypeAndName(CatagoryType catType, String name);

	List<Catagory> getCatagoryByTypeAndName(CatagoryType type, List<String> allNames);

	Catagory getCatagory(CatagoryType appointment, Long appointmentType);
}
