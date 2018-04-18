package com.lehanh.pama.old.model.v3;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Shell;

import com.lehanh.pama.old.model.BenhNhan;
import com.lehanh.pama.old.model.LanKhamBenh;
import com.lehanh.pama.old.model.PhauThuat;
import com.lehanh.pama.ui.util.FormManager;
import com.lehanh.pama.util.DateUtils;
import com.lehanh.pama.util.PamaHome;

public class Report {

	public static void main(String[] args) throws Exception {
		statictisBySurName();		
//		loadPerYear();
		
		//loadPerYear(1, 6, 2015);
//		loadPerYear(1, 2015);
//		loadPerYear(2, 2015);
//		loadPerYear(3, 2015);
//		loadPerYear(4, 2015);
//		loadPerYear(5, 2015);
//		loadPerYear(6, 2015);
//		loadPerYear(7, 2015);
//		loadPerYear(8, 2015);
//		loadPerYear(9, 2015);
//		loadPerYear(10, 2015);
//		loadPerYear(11, 2015);
//		loadPerYear(12, 2015);
	}
	
	private static final String[] filterBySurNames = new String[]
			{
			 Messages.Report_0,
			 Messages.Report_1,
			 Messages.Report_2,
			Messages.Report_3,
			Messages.Report_4,
			Messages.Report_5,
			Messages.Report_6,
			Messages.Report_7,
			Messages.Report_8,
			Messages.Report_9,
			Messages.Report_10,
			Messages.Report_11,
			Messages.Report_12,
			Messages.Report_13,
			Messages.Report_14,
			Messages.Report_15,
			Messages.Report_16,
			Messages.Report_17,
			Messages.Report_18

			};
	
	public static String statictisBySurName(/*int m, int year*/) throws Exception {
		//List<String[]> listBM = loadFile("olddata//bommo.csv");
		// phau thuat -> benh nhan, ds lan kham
		TreeMap<String, TreeMap<Long, BenhNhan>> statictis = new TreeMap<>();
		List<BenhNhan> listBN = ModelLoaderV3.getDSBenhNhan();
		listBN.stream().forEach(bn -> {
			for (LanKhamBenh lkb : bn.danhSachKham) {
				java.util.Date ngayKham;
				try {
					ngayKham = DateUtils.sqlDateToutilDate(lkb.ngayKham);
					int[] date = DateUtils.getDate(ngayKham);
					//if (/*(date[0] == 2015 && date[1] <= 6) || */(date[0] != year || date[1] != m)) {
					if (date[0] < 2012) {
						continue;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				for (PhauThuat pt : lkb.danhSachPhauThuat) {
					//System.out.println(t.id + "\t" + t.ten + "\t" + pt.ten + "\t" + pt.sym);
					final String ten = pt.ten;
					if (!Arrays.asList(filterBySurNames).contains(ten)) {
						continue;
					}
					TreeMap<Long, BenhNhan> staBN = statictis.get(ten);
					if (staBN == null) {
						staBN = new TreeMap<>(); statictis.put(ten, staBN);
					}
					staBN.put(bn.id, bn);
				}
			}
		});
		
		StringBuilder rs = new StringBuilder();
		int[] total = {0};
		statictis.forEach((pt, dsBn) -> {
			rs.append("Phau Thuat: " + pt + " - total: " + dsBn.size() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			total[0] += dsBn.size();
		});
		rs.append("Total all: " + total[0]); //$NON-NLS-1$
		System.out.println(rs);
		return rs.toString();
	}
	
	private static final String filterBySurName = "Bơm mỡ trên mắt"; //example "Bơm mỡ" //$NON-NLS-1$
	
	public static void loadPerYear(/*int m, int year*/) throws Exception {
		//List<String[]> listBM = loadFile("olddata//bommo.csv");
		// phau thuat -> benh nhan, ds lan kham
		TreeMap<String, List<StatictisBN>> statictis = new TreeMap<>();
		
		List<BenhNhan> listBN = ModelLoaderV3.getDSBenhNhan();
		listBN.stream().forEach(new Consumer<BenhNhan>() {

			@Override
			public void accept(BenhNhan t) {
				//System.out.println(t.id + "\t" + t.gioi);
				TreeMap<Integer, TreeSet<String>> statictisPerBN = new TreeMap<>();
				TreeMap<Integer, LanKhamBenh> lkbs = new TreeMap<>();
				
				for (LanKhamBenh lkb : t.danhSachKham) {
					try {
						java.util.Date ngayKham = DateUtils.sqlDateToutilDate(lkb.ngayKham);
						int[] date = DateUtils.getDate(ngayKham);
						//if (/*(date[0] == 2015 && date[1] <= 6) || */(date[0] != year || date[1] != m)) {
						if (date[0] < 2012) {
							continue;
						}
						
						lkbs.put(lkb.lanKham, lkb);
						
						TreeSet<String> allPT = statictisPerBN.get(lkb.lanKham);
						if (allPT == null) {
							allPT = new TreeSet<>();
							statictisPerBN.put(lkb.lanKham, allPT);
						}
						for (PhauThuat pt : lkb.danhSachPhauThuat) {
							//System.out.println(t.id + "\t" + t.ten + "\t" + pt.ten + "\t" + pt.sym);
							allPT.add(pt.ten);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				for (Entry<Integer, TreeSet<String>> entry : statictisPerBN.entrySet()) {
					for (String pt : entry.getValue()) {
						if (!StringUtils.containsIgnoreCase(pt, filterBySurName)) {
							continue;
						}
						//Integer cPT = statictis.get(pt);
						List<StatictisBN> bnsCurr = statictis.get(pt);
						if (bnsCurr == null) {
							bnsCurr = new LinkedList<>();
							statictis.put(pt, bnsCurr);
						}
						//cPT += 1;
						StatictisBN staticBN = new StatictisBN(t);
						LanKhamBenh lkb = lkbs.get(entry.getKey());
						staticBN.lkb.add(lkb);
						bnsCurr.add(staticBN);
					}
				}
			}
		});
		
		printContain(filterBySurName, statictis);
	}

	private static void printContain(String pt, TreeMap<String, List<StatictisBN>> statictis) {
		int total = 0;
		//int indexPT = 0;
		for (Entry<String, List<StatictisBN>> entry : statictis.entrySet()) {
			if (!StringUtils.containsIgnoreCase(entry.getKey(), pt)) {
				continue;
			}
			
			List<StatictisBN> listStatictics = entry.getValue();
			Collections.sort(listStatictics, new Comparator<StatictisBN>() {

				@Override
				public int compare(StatictisBN o1, StatictisBN o2) {
					if (o1.lkb.isEmpty() || o2.lkb.isEmpty()) {
						return 0;
					}
					return o2.lkb.get(0).ngayKham.compareTo(o1.lkb.get(0).ngayKham);
				}
			});
			
			System.out.println("Phẩu thuật : " + entry.getKey() + " (" + listStatictics.size() + " bệnh)"); //+ "\t" + entry.getValue()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
			listStatictics.forEach(new Consumer<StatictisBN>() {

				@Override
				public void accept(StatictisBN t) {
					String nk = ""; //$NON-NLS-1$
					
					//t.sortLKB();
					if (!t.lkb.isEmpty()) {
						
						// TODO DEBUG
						if (t.lkb.size() > 1) {
							System.out.println();
						}
						
						try {
							GregorianCalendar gc = new GregorianCalendar();
							gc.setTime(DateUtils.sqlDateToutilDate(t.lkb.get(0).ngayKham));
							nk = gc.get(Calendar.DAY_OF_MONTH) + "/" + (gc.get(Calendar.MONTH)+1) + "/" + gc.get(Calendar.YEAR); //$NON-NLS-1$ //$NON-NLS-2$
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					
					System.out.println(t.bn.id + " : " + t.bn.ten + " : " + nk); //$NON-NLS-1$ //$NON-NLS-2$
				}
			});
			System.out.println();
			
			total += entry.getValue().size();
		}
		
		//System.out.println("-----------------");
		System.out.println(total);
	}
}
