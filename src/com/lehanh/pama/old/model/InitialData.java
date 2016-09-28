package com.lehanh.pama.old.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.CatagoryType;
import com.lehanh.pama.catagory.DrugCatagory;
import com.lehanh.pama.catagory.PrescriptionCatagory;
import com.lehanh.pama.catagory.PrescriptionItem;
import com.lehanh.pama.db.DatabaseManager;
import com.lehanh.pama.db.dao.CatagoryDao;
import com.lehanh.pama.ui.util.MainApplication;
import com.lehanh.pama.util.PamaHome;

public class InitialData {

	private static TreeMap<CatagoryType, TreeMap<Long, Long>> insertedCat = new TreeMap<>();
	
	public static void main(String[] args) throws Exception {
		CatagoryDao catDao = new CatagoryDao();
		PamaHome.application = new MainApplication();
		DatabaseManager.initialize();
//		catDao.internalDeleteAll();
		
//		catDao.deleteAllType(CatagoryType.PROGNOSTIC);
//		catDao.deleteAllType(CatagoryType.DIAGNOSE);
//		catDao.deleteAllType(CatagoryType.SURGERY);
		
		loadDanhMuc("olddata", CatagoryType.PROGNOSTIC, catDao);
		loadDanhMuc("olddata", CatagoryType.DIAGNOSE, catDao);
		loadDanhMuc("olddata", CatagoryType.SURGERY, catDao);
		
		loadDanhMuc("olddata", CatagoryType.SERVICE, catDao);
		loadDanhMuc("olddata", CatagoryType.DR, catDao);
//		loadDanhMuc("olddata", CatagoryType.DRUG, catDao);
		loadDanhMuc("olddata", CatagoryType.ADVICE, catDao);
		
//		catDao.deleteAllType(CatagoryType.DRUG);
//		catDao.deleteAllType(CatagoryType.PRESCRIPTION);
//		loadDanhMuc("olddata", CatagoryType.DRUG, catDao);
//		loadDanhMuc("olddata", CatagoryType.PRESCRIPTION, catDao);
		
		//catDao.deleteAllType(CatagoryType.DR);
		//loadDanhMuc("olddata", CatagoryType.DR, catDao);

	}
	
	
	private static void loadDanhMuc(String dirFolder, CatagoryType catType, CatagoryDao catDao) throws IOException, SQLException {
		FileInputStream fis = new FileInputStream(dirFolder + "\\" + catType.name() + ".csv");
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (StringUtils.isBlank(line)) {
				break;
			}
			String[] data = line.split("\t");
			handleCatLine(catType, data, catDao);
		}
		br.close();
	}

	private static Map<String, DrugCatagory> allDrugCat = new TreeMap<>();
	private static Set<String> allDrugCatInserted = new HashSet<>();
	private static PrescriptionCatagory currPre = null;
	private static TreeMap<String, DrugCatagory> drugInserted = new TreeMap<>();
	private static void handleCatLine(CatagoryType catType, String[] data, CatagoryDao catDao) throws SQLException {
		if (CatagoryType.PRESCRIPTION == catType) {
			if (data.length == 1) {
				if ("end".equals(getData(data, 0))) {
					catDao.insert(currPre);
					for (DrugCatagory dC : allDrugCat.values()) {
						if (allDrugCatInserted.contains(dC.getName())) {
							continue;
						}
						catDao.insert(dC);
						allDrugCatInserted.add(dC.getName());
					}
					allDrugCat.clear();
					return;
				} else {
					currPre = new PrescriptionCatagory();
					currPre.setName(getData(data, 0));
					currPre.setDesc(getData(data, 0));
					currPre.setData(new LinkedList<>());
					return;
				}
			}
			//ct.cachSuDung, ct.cu, ct.donVi, ct.donViSuDung, ct.luuY, ct.ma, ""+ct.soLanSuDungTrenNgay, ""+ct.soLuong, ct.soLuongSuDungTrenLan, ct.ten
			int index = 0;
//			String drugName = getData(data, ]
			PrescriptionItem item = new PrescriptionItem(getData(data, index++), getData(data, index++), getData(data, index++), getData(data, index++), getData(data, index++), 
					// drugDesc
					getData(data, index++),
//					drugInserted.get
					Integer.parseInt(getData(data, index++)), Integer.parseInt(getData(data, index++)), getData(data, index++), getData(data, index));
			if (drugInserted.containsKey(item.getDrug())) {
				item.setDrugDesc(drugInserted.get(item.getDrug()).getDrugDesc());
			}
			String drugN = item.getDrug();
			DrugCatagory drug = allDrugCat.get(drugN);
			if (drug == null) {
				index = 0;
				drug = new DrugCatagory(getData(data, index++), getData(data, index++), getData(data, index++), getData(data, index++), getData(data, index++), getData(data, index++), 
						Integer.parseInt(getData(data, index++)), Integer.parseInt(getData(data, index++)), getData(data, index++), getData(data, index));
				allDrugCat.put(drugN, drug);
			}
			
			currPre.getData().add(item);
			return;
		}
		
		if (CatagoryType.DRUG == catType) {
			Long oldId = Long.valueOf(getData(data, 0));
			DrugCatagory drug = new DrugCatagory(catType, getData(data, 1), getData(data, 2), getData(data, 1), oldId);
			if (data.length >= 11) {
				drug.setDrugDesc(getData(data, 10));
			}
			//new DrugCatagory
			//(t.id + "\t" + t.ten + "\t" + t.cachSuDung + "\t" + t.cu + "\t" + t.donVi + "\t" + t.donViSuDung + "\t" + t.luuY + "\t" + t.soLanSuDungTrenNgay
			// + "\t" + t.soLuong + "\t" + t.soLuongSuDungTrenLan + "\t" + t.tenGoc);
			
			Long newId = catDao.insert(
					drug
					);
			drugInserted.put(drug.getName(), drug);
			updateCatCache(catType, oldId, newId);
			return;
		}
		
		if (CatagoryType.SURGERY == catType) {
			Long oldId = Long.valueOf(getData(data, 0));
			Long newId = catDao.insert(new Catagory(catType, getData(data, 1), getData(data, 2), getData(data, 1), oldId));
			updateCatCache(catType, oldId, newId);
			return;
		}
		
		if (CatagoryType.SERVICE == catType) {
			Long[] proList = null;
			if (!StringUtils.isBlank(getData(data, 1))) {
				String[] oldIds = getData(data, 1).split("\\|");
				proList = getListNewIds(CatagoryType.PROGNOSTIC, oldIds);
			}
			Long[] diaList = null;
			if (!StringUtils.isBlank(getData(data, 2))) {
				String[] oldIds = getData(data, 2).split("\\|");
				diaList = getListNewIds(CatagoryType.DIAGNOSE, oldIds);
			}
			Long[] surList = null;
			if (!StringUtils.isBlank(getData(data, 3))) {
				String[] oldIds = getData(data, 3).split("\\|");
				surList = getListNewIds(CatagoryType.SURGERY, oldIds);
			}

			catDao.insert(new Catagory(catType, getData(data, 0), getData(data, 0))
												.addRef(CatagoryType.PROGNOSTIC, proList)
												.addRef(CatagoryType.DIAGNOSE, diaList)
												.addRef(CatagoryType.SURGERY, surList));
			return;
		}
		
		Long oldId = Long.valueOf(getData(data, 0));
		Long newId = catDao.insert(new Catagory(catType, getData(data, 1), getData(data, 1), oldId));
		updateCatCache(catType, oldId, newId);
	}
	
	private static String getData(String[] data, int index) {
		String result = data[index];
		if (result == null) {
			return result;
		}
		return result.trim();
	}

	private static Long[] getListNewIds(CatagoryType prognostic, String[] oldIds) {
		List<Long> newIds = new LinkedList<>();
		for (String oldIdT : oldIds) {
			if (StringUtils.isBlank(oldIdT)) {
				continue;
			}
			newIds.add(insertedCat.get(prognostic).get(Long.valueOf(oldIdT)));
		}
		return newIds.toArray(new Long[newIds.size()]);
	}

	private static void updateCatCache(CatagoryType catType, Long oldId, Long newId) {
		TreeMap<Long, Long> cachePerType = insertedCat.get(catType);
		if (cachePerType == null) {
			cachePerType = new TreeMap<>();
			insertedCat.put(catType, cachePerType);
		}
		cachePerType.put(oldId, newId);
	}

}
