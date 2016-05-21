package com.lehanh.pama.catagory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.lehanh.pama.util.JsonMapper;

public class Catagory {

	private Long id;
	private Long oldId;
	private String name;
	private String desc;
	private String symbol;
	private final CatagoryType type;
	
	private String refIdsText; // Ex: 1|2|3
	private TreeMap<String, List<Long>> jsonParesd;
	//public static final String SEPARATE = "\\|";
	//private List<Long> refIds;
	
	private String otherDataText;
	
	public Catagory(CatagoryType type) {
		this.type = type;
	}
	
	public Catagory(Long id, CatagoryType type) {
		this.id = id;
		this.type = type;
	}
	
	public Catagory(CatagoryType catagoryType, String name, String desc) {
		this(null, catagoryType, name, null, desc, null);
	}
	
	public Catagory(CatagoryType catagoryType, String name, String desc, Long oldId) {
		this(null, catagoryType, name, null, desc, oldId);
	}
	
	public Catagory(CatagoryType catagoryType, String name, String symbol, String desc) {
		this(null, catagoryType, name, symbol, desc, null);
	}
	
	public Catagory(CatagoryType catagoryType, String name, String symbol, String desc, Long oldId) {
		this(null, catagoryType, name, symbol, desc, oldId);
	}
	
	public Catagory(Long id, CatagoryType catagoryType, String name, String symbol, String desc, Long oldId) {
		this(id, catagoryType);
		this.setName(name);
		this.setSymbol(symbol);
		this.setDesc(desc);
		this.oldId = oldId;
		//this.setRefIdsText(refIds);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long resultId) {
		this.id = resultId;
	}
	
	public CatagoryType getType() {
		return this.type;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		if (symbol == null) {
			return this.getName();
		}
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getRefIdsText() {
		return refIdsText;
	}

	public void setRefIdsText(String refIdsText) {
		this.refIdsText = refIdsText;
		//this.refIdsText = StringUtils.remove(refIdsText, StringUtils.SPACE);
	}

	public Catagory addRef(CatagoryType type, Long... id) {
		// if (id == null) {
		// throw new PamaException("Cannot add null Ref id");
		// }
		// if (StringUtils.isBlank(refIdsText)) {
		// refIdsText = String.valueOf(id);
		// } else {
		// refIdsText += SEPARATE + String.valueOf(id);
		// }
		// refIds = null;
		if (id == null || id.length == 0 || type == null) {
			//throw new PamaException("Cannot add null Ref id or not indentify CatType");
			return this;
		}
		
		getRefIds();
		if (this.jsonParesd == null) {
			this.jsonParesd = new TreeMap<>();
		}
		List<Long> refPerType = this.jsonParesd.get(type.name());
		if (refPerType == null) {
			refPerType = new LinkedList<>();
			this.jsonParesd.put(type.name(), refPerType);
		}
		refPerType.addAll(Arrays.asList(id));
		
		refIdsText = JsonMapper.toJson(this.jsonParesd);
		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public TreeMap<String, List<Long>> getRefIds() {
		this.jsonParesd = null;
		if (!StringUtils.isBlank(this.refIdsText)) {
			this.jsonParesd = JsonMapper.fromJson(refIdsText, TreeMap.class);
		}
		return this.jsonParesd;
		/*if (refIds == null) {
			refIds = new LinkedList<Long>();
			if (!StringUtils.isBlank(refIdsText)) {
				String[] refIdsA = refIdsText.split(SEPARATE);
				try {
					for (String refIDText : refIdsA) {
						refIds.add(Long.parseLong(refIDText));
					}
				} catch (NumberFormatException e) {
					throw new PamaException("Cannot generate long as text from Catalog getRefIds " + refIdsText, e);
				}
			}
		}
		return new LinkedList<Long>(refIds);*/
	}
	
	public String getOtherDataText() {
		return otherDataText;
	}

	public void setOtherDataText(String otherDataText) {
		this.otherDataText = otherDataText;
	}

	public static List<String> getListName(List<? extends Catagory> cats) {
		if (cats == null) {
			return null;
		}
		List<String> result = new LinkedList<String>();
		for (Catagory cat : cats) {
			result.add(cat.getName());
		}
		return result;
	}

	public Long getOldId() {
		if (oldId == null) {
			return -1l;
		}
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

}