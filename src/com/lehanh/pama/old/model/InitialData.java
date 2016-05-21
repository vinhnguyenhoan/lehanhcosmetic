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
		catDao.internalDeleteAll();
		
		loadDanhMuc("olddata", CatagoryType.PROGNOSTIC, catDao);
		loadDanhMuc("olddata", CatagoryType.DIAGNOSE, catDao);
		loadDanhMuc("olddata", CatagoryType.SURGERY, catDao);
		loadDanhMuc("olddata", CatagoryType.SERVICE, catDao);
		loadDanhMuc("olddata", CatagoryType.DR, catDao);
		loadDanhMuc("olddata", CatagoryType.DRUG, catDao);
		loadDanhMuc("olddata", CatagoryType.ADVICE, catDao);
		
//		catDao.deleteAllType(CatagoryType.PRESCRIPTION);
		loadDanhMuc("olddata", CatagoryType.PRESCRIPTION, catDao);
		
//		catDao.deleteAllType(CatagoryType.DR);
		loadDanhMuc("olddata", CatagoryType.DR, catDao);

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
	private static void handleCatLine(CatagoryType catType, String[] data, CatagoryDao catDao) throws SQLException {
		if (CatagoryType.PRESCRIPTION == catType) {
			if (data.length == 1) {
				if ("end".equals(data[0])) {
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
					currPre.setName(data[0]);
					currPre.setDesc(data[0]);
					currPre.setData(new LinkedList<>());
					return;
				}
			}
			//ct.cachSuDung, ct.cu, ct.donVi, ct.donViSuDung, ct.luuY, ct.ma, ""+ct.soLanSuDungTrenNgay, ""+ct.soLuong, ct.soLuongSuDungTrenLan, ct.ten
			int index = 0;
			PrescriptionItem item = new PrescriptionItem(data[index++], data[index++], data[index++], data[index++], data[index++], data[index++], 
					Integer.parseInt(data[index++]), Integer.parseInt(data[index++]), data[index++], data[index]);
			
			String drugN = item.getDrug();
			DrugCatagory drug = allDrugCat.get(drugN);
			if (drug == null) {
				index = 0;
				drug = new DrugCatagory(data[index++], data[index++], data[index++], data[index++], data[index++], data[index++], 
						Integer.parseInt(data[index++]), Integer.parseInt(data[index++]), data[index++], data[index]);
				allDrugCat.put(drugN, drug);
			}
			
			currPre.getData().add(item);
			return;
		}
		
		if (CatagoryType.DRUG == catType) {
			Long oldId = Long.valueOf(data[0]);
			Long newId = catDao.insert(
					new Catagory
					(catType, data[1], data[2], data[1], oldId)
					//new DrugCatagory
					//(t.id + "\t" + t.ten + "\t" + t.cachSuDung + "\t" + t.cu + "\t" + t.donVi + "\t" + t.donViSuDung + "\t" + t.luuY + "\t" + t.soLanSuDungTrenNgay
					// + "\t" + t.soLuong + "\t" + t.soLuongSuDungTrenLan + "\t" + t.tenGoc);
					);

			updateCatCache(catType, oldId, newId);
			return;
		}
		
		if (CatagoryType.SURGERY == catType) {
			Long oldId = Long.valueOf(data[0]);
			Long newId = catDao.insert(new Catagory(catType, data[1], data[2], data[1], oldId));
			updateCatCache(catType, oldId, newId);
			return;
		}
		
		if (CatagoryType.SERVICE == catType) {
			Long[] proList = null;
			if (!StringUtils.isBlank(data[1])) {
				String[] oldIds = data[1].split("\\|");
				proList = getListNewIds(CatagoryType.PROGNOSTIC, oldIds);
			}
			Long[] diaList = null;
			if (!StringUtils.isBlank(data[2])) {
				String[] oldIds = data[2].split("\\|");
				diaList = getListNewIds(CatagoryType.DIAGNOSE, oldIds);
			}
			Long[] surList = null;
			if (!StringUtils.isBlank(data[3])) {
				String[] oldIds = data[3].split("\\|");
				surList = getListNewIds(CatagoryType.SURGERY, oldIds);
			}

			catDao.insert(new Catagory(catType, data[0], data[0])
												.addRef(CatagoryType.PROGNOSTIC, proList)
												.addRef(CatagoryType.DIAGNOSE, diaList)
												.addRef(CatagoryType.SURGERY, surList));
			return;
		}
		
		Long oldId = Long.valueOf(data[0]);
		Long newId = catDao.insert(new Catagory(catType, data[1], data[1], oldId));
		updateCatCache(catType, oldId, newId);
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
