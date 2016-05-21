package com.lehanh.pama.catagory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lehanh.pama.ICatagoryManager;
import com.lehanh.pama.db.dao.CatagoryDao;

public class CatagoryManager implements ICatagoryManager {

	private final Map<CatagoryType, TreeMap<Long, Catagory>> allCatagory = new HashMap<CatagoryType, TreeMap<Long, Catagory>>();
	
	/*private class TextAndNumberKey {
		
		private String text;
		private Long number;
		
		@Override
		public int hashCode() {
			return (text == null ? 0 : text.hashCode()) + (number == null ? 0 : number.hashCode());
		}
		
		@Override
		public boolean equals(Object other) {
			if (other == null) {
				return false;
			}
			if (getClass() != other.getClass()) {
	            return false;
			}
			
			TextAndNumberKey otherTaN = (TextAndNumberKey) other;
			if (otherTaN.text == null && otherTaN.number == null) {
				return false;
			}
			
			return (otherTaN.text == null ? true : otherTaN.text.equals(text)) && (otherTaN.number == null ? true : otherTaN.number.equals(number));
		}
	}*/
	
	@Override
	public void initialize() throws SQLException {
		List<Catagory> allCat = new CatagoryDao().loadAllCatagory();
		loadCatList(allCat);
		
		// Load internal catagory
		InternalCatagory internalCat;
		for (CatagoryType catType : CatagoryType.values()) {
			Catagory createdCat = catType.createCatalog(null);
			if ((createdCat instanceof InternalCatagory)) {
				internalCat = (InternalCatagory) createdCat;
				List<Catagory> catDatas = internalCat.createCatagoryList();
				loadCatList(catDatas);
			}
		}
	}

	private void loadCatList(List<Catagory> allCat) {
		// TODO sort list
		TreeMap<Long, Catagory> catByType;
		for (Catagory cat : allCat) {
			catByType = allCatagory.get(cat.getType());
			if (catByType == null) {
				catByType = new TreeMap<Long, Catagory>();
				allCatagory.put(cat.getType(), catByType);
			}
			catByType.put(cat.getId(), cat);
			if (cat instanceof IContainJsonDataCatagory) {
				((IContainJsonDataCatagory) cat).updateFromText();
			}
		}
	}

	@Override
	public Catagory getCatagoryByTypeAndName(CatagoryType catType, String name) {
		if (StringUtils.isBlank(name) || catType == null) {
			return null;
		}
		TreeMap<Long, Catagory> catByType = this.allCatagory.get(catType);
		if (catByType == null) {
			return null;
		}
		
		for (Entry<Long, Catagory> entry : catByType.entrySet()) {
			if (name.equals(entry.getValue().getName())) {
				return entry.getValue();
			}
		}
		
		return null;
	}
	
	@Override
	public TreeMap<Long, Catagory> getCatagoryByType(CatagoryType catType) {
		TreeMap<Long, Catagory> catByType = this.allCatagory.get(catType);
		if (catByType == null) {
			return new TreeMap<Long, Catagory>();
		}
		return new TreeMap<Long, Catagory>(catByType);
	}
	
	@Override
	public Catagory getCatagory(CatagoryType catType, Long id) {
		TreeMap<Long, Catagory> catByType = this.allCatagory.get(catType);
		if (catByType == null) {
			return null;
		}
		return catByType.get(id);
	}
	
	@Override
	public List<Catagory> getCatagoryByTypeAndName(CatagoryType type, List<String> allNames) {
		List<Catagory> result = new LinkedList<Catagory>();
		if (allNames == null) {
			return result;
		}
		
		for (String name : allNames) {
			Catagory cat = getCatagoryByTypeAndName(type, name);
			if (cat == null) {
				continue;
			}
			result.add(cat);
		}
		return result;
	}

	@Override
	public List<Catagory> getSubCatagorysByParent(CatagoryType parentType, Long parentId, CatagoryType type) {
		final List<Catagory> result = new LinkedList<Catagory>();
		
		//CatagoryType parentType = type.getParentCatagoryType();
		if (parentType == null) {
			return result;
		}
		TreeMap<Long, Catagory> catByType = this.allCatagory.get(parentType);
		if (catByType == null) {
			return result;
		}
		Catagory parentCat = catByType.get(parentId);
		if (parentCat == null) {
			return result;
		}
		
		TreeMap<String, List<Long>> refMapIds = parentCat.getRefIds();
		if (refMapIds == null) {
			return result;
		}
		List<Long> refIds = refMapIds.get(type.name());
		TreeMap<Long, Catagory> refList = allCatagory.get(type);
		if (refList == null || refIds == null) {
			return result;
		}
		Catagory refCatagory;
		for (Number refId : refIds) {
			refCatagory = refList.get(refId.longValue());
			result.add(refCatagory);
		}
		
		return result;
	}
	
	@Override
	public void saveCatagory(Catagory cat) throws SQLException {
		if (cat instanceof IContainJsonDataCatagory) {
			cat.setOtherDataText(((IContainJsonDataCatagory) cat).toOtherDataAsText());
		}
		if (cat.getId() == null || cat.getId() < 0) {
			new CatagoryDao().insert(cat);
		} else {
			new CatagoryDao().update(cat);
		}
		loadCatList(Arrays.asList(cat));
		//allCatagory.get(cat.getType()).put(cat.getId(), cat);
	}
	
	public static void main(String[] args) {
		final GsonBuilder builder = new GsonBuilder();
	    final Gson gson = builder.create();
	    DrugCatagory d = new DrugCatagory();
	    d.setUnit("Viên"); //$NON-NLS-1$
	    String json = gson.toJson(d);
	    System.out.println(json); 
	    
	    DrugCatagory jsonObj = gson.fromJson(json, DrugCatagory.class);
	    System.out.println(jsonObj.getUnit());
	    
	    json = gson.toJson(jsonObj);
	    System.out.println(json); 
	    
	    json = null;
	    System.out.println(gson.fromJson(json, HashMap.class));
	}

}