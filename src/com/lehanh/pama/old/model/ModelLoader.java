package com.lehanh.pama.old.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

public class ModelLoader {
	
	/*
Bơm mỡ dưới mắt 	Lower Eye fat injection
Bơm mỡ dưới mắt, má, thái dương	Lower Eye, Cheek, Temp fat injection
Bơm mỡ trên mắt 	Upper Eye fat injection

Bơm mỡ má + mắt

Bơm mỡ vào vùng thái dương	Temp fat injection
Bơm mỡ vào rãnh mũi má	N L fat injection
Bơm mỡ vào má	Cheek fat injection
Bơm mỡ vào vùng lõm ở ngực	Deep-set breast fat injection
Bơm mỡ vào vùng lõm ở mông	Deep-set buttock fat injection
Bơm mỡ vào mu bàn tay	Hand fat injection
Bơm mỡ vào ngón tay	Finger fat injection
Bơm mỡ vào sẹo lõm	Scar fat injection
Bơm mỡ vào mu	Pubis fat injection
Bơm mỡ cằm	Chin fat injection
Bơm mỡ vào môi	Lip fat injection 
Bơm mỡ vào vùng lõm trên mặt	Face fat injection
Bơm mỡ vào trán	Forehead Fat Injection
Bơm mỡ vào mũi	Nose fat injection
	 */
	
	static String[] toathuocmau = new String[]{
		"Căng da mặt",
		"Căng trán thái dương",
		"Chỉnh hình mí - Treo cung mày",
		"Chỉnh hình mũi Fascia",
		"Chỉnh hình mũi Silas",
		"Chỉnh hình mũi Sili",
		"Đặt túi ngực",
		"Đau dây thần kinh sau mổ",
		"Đau nhức do tổn thương thần kinh",
		"Hậu phẫu chỉnh hình mũi, mắt",
		"Sili môi"
	};
	
	public static void main(String[] args) throws IOException {
		List<String[]> listBM = loadFile("olddata//bommo.csv");
		
		TreeMap<String, Integer> statictisBommo = new TreeMap<>();
		
		List<BenhNhan> listBN = ModelLoader.getDSBenhNhan();
		listBN.stream().forEach(new Consumer<BenhNhan>() {

			@Override
			public void accept(BenhNhan t) {
				//System.out.println(t.id + "\t" + t.ten);
				for (LanKhamBenh lkb : t.danhSachKham) {
					//lkb.ngayKham
					for (PhauThuat pt : lkb.danhSachPhauThuat) {
						System.out.println(t.id + "\t" + t.ten + "\t" + pt.ten);
						
					}
				}
			}
		});
	}
	
	public static List<String[]> loadFile(String dirFolder) throws IOException {
		FileInputStream fis = new FileInputStream(dirFolder);
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		List<String[]> result = new LinkedList<>();
		while ((line = br.readLine()) != null) {
			if (StringUtils.isBlank(line)) {
				break;
			}
			String[] data = line.split("\t");
			result.add(data);
		}
		br.close();
		return result;
	}
	
	public static void loadToaThuocMau() {
		List<ToaThuocMau> list = ModelLoader.getDSToaThuocMau();
		list.stream().forEach(new Consumer<ToaThuocMau>() {

			@Override
			public void accept(ToaThuocMau t) {
				boolean showNamed = false;
				for (String ft : toathuocmau) {
					if (t.ten.equals(ft)) {
						if (!showNamed) {
							System.out.println(t.ten);
							showNamed = true;
						}
						for (ChiTietToaThuoc ct : t.dsChiTiet) {
							System.out.println(print(ct.cachSuDung, ct.cu, ct.donVi, ct.donViSuDung, /*""+ct.id,*/ ct.luuY, ct.ma
									, ""+ct.soLanSuDungTrenNgay, ""+ct.soLuong, ""+ct.soLuongSuDungTrenLan, ct.ten));
						}
						System.out.println("end");
					}
				}
			}
		});
	}

	protected static String print(String... texts) {
		String result = "";
		int index = 0;
		for (String text : texts) {
			result += text;
			if (index < texts.length - 1) {
				result += "\t";
			}
			index++;
		}
		return result;
	}

	public static void printThuoc() {
		List<Thuoc> list = ModelLoader.getDSThuoc();
		list.stream().forEach(new Consumer<Thuoc>() {

			@Override
			public void accept(Thuoc t) {
				System.out.println(t.id + "\t" + t.ten + "\t" + t.ma);
			}
		});
	}
	
	static int count = 0;
	public static void printBN() {
		List<BenhNhan> listBN = ModelLoader.getDSBenhNhan();
		
		listBN.stream().forEach(new Consumer<BenhNhan>() {

			@Override
			public void accept(BenhNhan t) {
				//System.out.println(t.id + "\t" + t.ten);
				for (LanKhamBenh lkb : t.danhSachKham) {
					if (lkb.danhSachPhauThuat.size() > 0) {
						count += 1;
					}
					for (PhauThuat pt : lkb.danhSachPhauThuat) {
						System.out.println(t.id + "\t" + t.ten + "\t" + pt.ten);
					}
				}
			}
		});
		System.out.println("TOng so " + count);
	}
	
	public static void printCatalog() {
//		List<DanhMuc> list = ModelLoader.getDSPhauThuat();
//		list.stream().forEach(new Consumer<DanhMuc>() {
//
//			@Override
//			public void accept(DanhMuc t) {
//				System.out.println(t.id + "\t" + t.ten + "\t" + t.sym);
//			}
//		});
		
		List<BacSy> listBS = ModelLoader.getDSBacSy();
		listBS.stream().forEach(new Consumer<BacSy>() {

			@Override
			public void accept(BacSy t) {
				System.out.println(t.id + "\t" + t.ten + "\t" + t.sym);
			}
		});
		
	}
	private static final String HOST = "9WZY502";
	private static final String DB_NAME = "LHS";
	private static final String USER = "sa";
	private static final String PASS = "kms1895@3";
	
	private static Connection conn;
	public static Connection getSession() throws SQLException, ClassNotFoundException {
		if (conn != null && !conn.isClosed()) {
			return conn;
		}
		String url = String.format("jdbc:sqlserver://%s;databaseName=%s", HOST, DB_NAME);
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		conn = DriverManager.getConnection(url, USER, PASS);;
		return conn;
	}
	
	public static void close() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.close();
		}
	}

	private static List<BacSy> drList = null;
	private static Map<Long, BacSy> drMap = new HashMap<Long, BacSy>();
	public static List<BacSy> getDSBacSy() {
		if (drList != null) {
			return drList;
		}
		try {
			Connection conn = getSession();
			PreparedStatement ps = conn.prepareStatement("SELECT empISN, empName FROM dbo.Employees");
			ResultSet re = ps.executeQuery();
			drList = new ArrayList<BacSy>();
			while (re.next()) {
				BacSy dr = new BacSy(
						re.getLong("empISN"), 
						re.getString("empName"));
				drList.add(dr);
				drMap.put(dr.getId(), dr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drList;
	}
	
	public static BacSy getBacSy(Long id) {
		if (drList == null) {
			getDSBacSy();
		}
		return drMap.get(id);
	}

	private static List<DanhMuc> dsTrieuChung = null;
	private static Map<Long, DanhMuc> trieuChungMap = new HashMap<Long, DanhMuc>();
	public static List<DanhMuc> getDSTrieuChung() {
		if (dsTrieuChung != null) {
			return dsTrieuChung;
		}
		try {
			Connection conn = getSession();
			PreparedStatement ps = conn.prepareStatement("select sckISN, sckName from dbo.Sicks where sktISN=13");
			ResultSet re = ps.executeQuery();
			dsTrieuChung = new ArrayList<DanhMuc>();
			while (re.next()) {
				DanhMuc dr = new DanhMuc(
						re.getLong("sckISN"), 
						re.getString("sckName"));
				dsTrieuChung.add(dr);
				trieuChungMap.put(dr.id, dr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsTrieuChung;
	}
	
	public static DanhMuc getTrieuChung(Long id) {
		if (dsTrieuChung == null) {
			getDSTrieuChung();
		}
		return trieuChungMap.get(id);
	}
	
	private static List<DanhMuc> dsChuanDoan = null;
	private static Map<Long, DanhMuc> mapChuanDoan = new HashMap<Long, DanhMuc>();
	public static List<DanhMuc> getDSChuanDoan() {
		if (dsChuanDoan != null) {
			return dsChuanDoan;
		}
		try {
			Connection conn = getSession();
			PreparedStatement ps = conn.prepareStatement("select plsISN,plsPathologicalSigns from PathologicalSigns where pltISN=12;");
			ResultSet re = ps.executeQuery();
			dsChuanDoan = new ArrayList<DanhMuc>();
			while (re.next()) {
				DanhMuc dr = new DanhMuc(
						re.getLong("plsISN"), 
						re.getString("plsPathologicalSigns"));
				dsChuanDoan.add(dr);
				mapChuanDoan.put(dr.id, dr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsChuanDoan;
	}
	
	public static DanhMuc getChuanDoan(Long id) {
		if (dsChuanDoan == null) {
			getDSChuanDoan();
		}
		return mapChuanDoan.get(id);
	}
	
	private static List<DanhMuc> dsPhauThuat = null;
	private static Map<String, DanhMuc> mapPhauThuat = new HashMap<String, DanhMuc>();
	public static List<DanhMuc> getDSPhauThuat() {
		if (dsPhauThuat != null) {
			return dsPhauThuat;
		}
		try {
			Connection conn = getSession();
			PreparedStatement ps = conn.prepareStatement("select "
					+ "surISN,surName,surSymbol "
					//+ "*"
					+ "from dbo.Surgical");
			ResultSet re = ps.executeQuery();
			dsPhauThuat = new ArrayList<DanhMuc>();
			while (re.next()) {
				DanhMuc dr = new DanhMuc(
						re.getLong("surISN"), 
						re.getString("surName"),
						re.getString("surSymbol"));
				dsPhauThuat.add(dr);
				mapPhauThuat.put(dr.ten, dr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsPhauThuat;
	}
	
	public static DanhMuc getPhauThuat(String name) {
		if (dsPhauThuat == null) {
			getDSPhauThuat();
		}
		return mapPhauThuat.get(name);
	}

	private static List<ToaThuocMau> dsToaThuocMau = null;
	private static Map<String, ToaThuocMau> mapToaThuocMau = new HashMap<String, ToaThuocMau>();
	public static List<ToaThuocMau> getDSToaThuocMau() {
		if (dsToaThuocMau != null) {
			return dsToaThuocMau;
		}
		try {
			Connection conn = getSession();
			PreparedStatement ps = conn.prepareStatement("select  samISN,mimID,samSickName,MexMedicineName,mexUsage,mexQuantityPerUnit," +
					"mexTakePerDay,mexQuantityPerDay,mexUsagePerDay,mexUsageUnit,mexNote from SamplePrescription order by samSickName;");
			ResultSet re = ps.executeQuery();
			dsToaThuocMau = new ArrayList<ToaThuocMau>();
			while (re.next()) {
				String samName = re.getString("samSickName");
				ToaThuocMau rc = mapToaThuocMau.get(samName);
				if (rc == null) {
					rc = new ToaThuocMau(samName);
					dsToaThuocMau.add(rc);
					mapToaThuocMau.put(rc.ten, rc);
				}
				ChiTietToaThuoc d = new ChiTietToaThuoc(
						re.getLong("samISN"), 
						re.getString("MexMedicineName"),		
						re.getString("mimID"),		
						re.getInt("mexQuantityPerUnit"), 
						re.getString("mexUsageUnit"),		
						re.getInt("mexTakePerDay"), 
						re.getString("mexQuantityPerDay"), 
						re.getString("mexUsageUnit"),		
						re.getString("mexUsagePerDay"),		
						re.getString("mexUsage"),		
						re.getString("mexNote")		
				);
				rc.dsChiTiet.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsToaThuocMau;
	}
	
	public static ToaThuocMau getToaThuocMau(String tenMau) {
		if (dsToaThuocMau == null) {
			getDSToaThuocMau();
		}
		return mapToaThuocMau.get(tenMau);
	}
	
	private static List<Thuoc> dsThuoc = null;
	private static Map<Long, Thuoc> mapThuoc = new HashMap<Long, Thuoc>();
	public static List<Thuoc> getDSThuoc() {
		if (dsThuoc != null) {
			return dsThuoc;
		}
		try {
			Connection conn = getSession();
			PreparedStatement ps = conn.prepareStatement("select mesISN,mimID,mesMedicineName from dbo.MedicineStock");
			ResultSet re = ps.executeQuery();
			dsThuoc = new ArrayList<Thuoc>();
			while (re.next()) {
				Thuoc dr = new Thuoc(
						re.getLong("mesISN"), 
						re.getString("mimID"),		
						re.getString("mesMedicineName")		
				);
				dsThuoc.add(dr);
				mapThuoc.put(dr.id, dr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsThuoc;
	}
	
	public static Thuoc getThuoc(Long id) {
		if (dsThuoc == null) {
			getDSThuoc();
		}
		return mapThuoc.get(id);
	}

	public static List<BenhNhan> getDSBenhNhan() {
		Map<Long, LanKhamBenh> mapLanKham = new HashMap<Long, LanKhamBenh>();
		List<BenhNhan> list = new ArrayList<BenhNhan>();
		try {
			Connection conn = getSession();
			PreparedStatement ps = conn.prepareStatement("SELECT patID,patName,patBirthDate,patAge,patSex,patAddress,patTel,patMobile,patOccupation, "+
				"case when isnull(patHasPic,0)=1 then 'PatientPic\\'+patID+'.jpg' else '' end patImg, "+
				"clrISN,clrMedicalHistory, clr.clrSickHistory,clr.clrSickChange,clr.clrPulse,clr.clrBloodPressure, "+
				"clr.clrTemperature,clr.clrWeigh,clr.empISN,clr.clrSickNotInList,clr.clrPathologicalSigns, "+
				"clr.clrSurgery,clr.clrRexamination4Surgery,clr.clrSkill,clr.clrExaminationTimes,clr.clrRexaminationTimes, "+
				"clr.clrExaminationDate,clr.clrFollowUpExaminationDate,clrMedicalAdvice "+
				"FROM Patients pt left join ClinicalRecord clr on pt.patISN = clr.patISN "+
				"where exists ( "+
				"SELECT 1 FROM ClinicalRecord clr where exists ( "+
				"select 1 from dbo.ClinicalSicks cls where exists (select 1 from Sicks where sktISN=13 and cls.sckISN=sckISN) "+
				"and clr.clrISN=cls.clrISN ) and pt.patISN = clr.patISN ) order by patID,clrExaminationTimes,clrRexaminationTimes;");
			ResultSet re = ps.executeQuery();
			BenhNhan benhNhan = null;
			LanKhamBenh lanKham = null;
			while (re.next()) {
				if (benhNhan == null || benhNhan.id != re.getLong("patID")) {
					benhNhan = new BenhNhan(
							re.getLong("patID"), 
							re.getString("patName"),		
							re.getDate("patBirthDate"),
							re.getInt("patAge"),
							re.getString("patSex"),		
							re.getString("patAddress"),		
							re.getString("patTel"),		
							re.getString("patMobile"),		
							re.getString("patOccupation"),		
							re.getString("patImg")		
					);
					list.add(benhNhan);
				}
				if (lanKham == null || lanKham.id != re.getLong("clrISN")) {
					lanKham = new LanKhamBenh(
							re.getLong("clrISN"), 
							re.getString("clrMedicalHistory"),
							false, false, false,
							re.getString("clrSickHistory"),
							re.getString("clrSickChange"),
							re.getString("clrPulse"),		
							re.getString("clrBloodPressure"),		
							re.getString("clrTemperature"),		
							re.getString("clrWeigh"),		
							getBacSy(re.getLong("empISN")),		
							re.getString("clrSickNotInList"),	
							null,
							re.getString("clrSkill"),		
							re.getInt("clrExaminationTimes"),		
							re.getInt("clrRexaminationTimes"),		
							re.getDate("clrExaminationDate"),		
							re.getDate("clrFollowUpExaminationDate"),		
							re.getString("clrMedicalAdvice")		
					);
					benhNhan.addLanKhamBenh(lanKham);
					mapLanKham.put(lanKham.id, lanKham);
					String sur = re.getString("clrSurgery");
					String surRe = re.getString("clrRexamination4Surgery");
					if (sur != null && surRe != null) {
						String surs[] = sur.split(" -");
						String surRes[] = surRe.split(";");
						for (int i = 0; i < surs.length - 1; i++) {
							String st = surs[i].trim();
							if (st.contains(":")) {
								String[] ss = st.split(":");
								if (ss.length > 1) {
									st = ss[1].trim();
								} else {
									st = ss[0].trim();
								}
							}
							DanhMuc s = getPhauThuat(st);
							PhauThuat pt = new PhauThuat();
							if (s != null) {
								pt.id = s.id;
								pt.ten = s.ten;
							} else {
								pt.id = 0;
								pt.ten = st;
							}
							if (i < surRes.length) {
								pt.taiKham = surRes[i].equals("1");
							}
							lanKham.danhSachPhauThuat.add(pt);
						}
					}
				}
			}
			ps = conn.prepareStatement("SELECT OtherID1,colCaoHuyetAp,colTangDuongHuyet,colViemGan FROM tableTienCan_20100210110927625244 tc WHERE exists (" +
				"select 1 from dbo.ClinicalSicks cls where exists (select 1 from Sicks where sktISN=13 and cls.sckISN=sckISN) and tc.OtherID1=cls.clrISN );");
			re = ps.executeQuery();
			while (re.next()) {
				LanKhamBenh lk = mapLanKham.get(re.getLong("OtherID1"));
				if (lk != null) {
					lk.caoHuyetAp = re.getBoolean("colCaoHuyetAp");
					lk.tangDuongHuyet = re.getBoolean("colTangDuongHuyet");
					lk.viemGan = re.getBoolean("colViemGan");
				}
			}
			ps = conn.prepareStatement("select  clrISN,plsISN,plsPathologicalSigns,polPathologicalSignsOutList from vw_PathologicalClinical clr where exists (" +
				"select 1 from dbo.ClinicalSicks cls where exists (select 1 from Sicks where sktISN=13 and cls.sckISN=sckISN) and clr.clrISN=cls.clrISN )");
			re = ps.executeQuery();
			while (re.next()) {
				LanKhamBenh lk = mapLanKham.get(re.getLong("clrISN"));
				if (lk != null) {
					lk.chuanDoanNgoaiDS = re.getString("polPathologicalSignsOutList");
					DanhMuc cd = getChuanDoan(re.getLong("plsISN"));
					if (cd != null) {
						lk.danhSachChuanDoan.add(cd);
					} else if (re.getLong("plsISN") != 0) {
						lk.danhSachChuanDoan.add(new DanhMuc(
								re.getLong("plsISN"), 
								re.getString("plsPathologicalSigns")
								));
					}
				}
			}
			ps = conn.prepareStatement("select clrISN,sckISN,sckName from vw_ClinicalSicks cl where exists (" +
				"select 1 from dbo.ClinicalSicks cls where exists (select 1 from Sicks where sktISN=13 and cls.sckISN=sckISN) and cl.clrISN=cls.clrISN )");
			re = ps.executeQuery();
			while (re.next()) {
				LanKhamBenh lk = mapLanKham.get(re.getLong("clrISN"));
				if (lk != null) {
					DanhMuc tc = getTrieuChung(re.getLong("sckISN"));
					if (tc != null) {
						lk.danhSachTrieuChung.add(tc);
					} else if (re.getLong("sckISN") != 0) {
						lk.danhSachTrieuChung.add(new DanhMuc(
								re.getLong("sckISN"), 
								re.getString("sckName")
								));
					}
				}
			}
			ps = conn.prepareStatement("select clrISN,mesISN,mexMedicineName,mexUsage,mexQuantityPerUnit,mexTakePerDay,mexQuantityPerDay,mexUsagePerDay,mexUsageUnit,mexNote from dbo.MedicineExport me where exists (" +
			"select 1 from dbo.ClinicalSicks cls where exists (select 1 from Sicks where sktISN=13 and cls.sckISN=sckISN) and me.clrISN=cls.clrISN )");
			re = ps.executeQuery();
			while (re.next()) {
				LanKhamBenh lk = mapLanKham.get(re.getLong("clrISN"));
				if (lk != null) {
					ChiTietToaThuoc d = new ChiTietToaThuoc(
							re.getLong("mesISN"), 
							re.getString("MexMedicineName"),		
							re.getString("mesISN"),		
							re.getInt("mexQuantityPerUnit"), 
							re.getString("mexUsageUnit"),		
							re.getInt("mexTakePerDay"), 
							re.getString("mexQuantityPerDay"), 
							re.getString("mexUsageUnit"),		
							re.getString("mexUsagePerDay"),		
							re.getString("mexUsage"),		
							re.getString("mexNote")		
					);
					lk.danhSachChiTietToaThuoc.add(d);
				}
			}
			ps = conn.prepareStatement("SELECT clrISN,clpISN,clpPicName,clpPicture,clpPicDate FROM [ClinicalPicture] clp where exists (" +
			"select 1 from dbo.ClinicalSicks cls where exists (select 1 from Sicks where sktISN=13 and cls.sckISN=sckISN) and clp.clrISN=cls.clrISN )");
			re = ps.executeQuery();
			while (re.next()) {
				LanKhamBenh lk = mapLanKham.get(re.getLong("clrISN"));
				if (lk != null) {
					HinhAnh h = new HinhAnh(
							re.getLong("clpISN"), 
							re.getString("clpPicName"),		
							re.getString("clpPicture"),		
							re.getDate("clpPicDate")		
					);
					lk.danhSachHinhAnh.add(h);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private static List<DanhMuc> dsLoiKhuyen = null;
	public static List<DanhMuc> getDSLoiKhuyen() {
		if (dsLoiKhuyen != null) {
			return dsLoiKhuyen;
		}
		try {
			Connection conn = getSession();
			PreparedStatement ps = conn.prepareStatement("select  advISN,advDoctorAdvice from Advice");
			ResultSet re = ps.executeQuery();
			dsLoiKhuyen = new ArrayList<DanhMuc>();
			while (re.next()) {
				DanhMuc dr = new DanhMuc(
						re.getLong("advISN"), 
						re.getString("advDoctorAdvice"));
				dsLoiKhuyen.add(dr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsLoiKhuyen;
	}

	private static List<DanhMuc> dsThuThuat = null;
	public static List<DanhMuc> getDSThuThuat() {
		if (dsThuThuat != null) {
			return dsThuThuat;
		}
		try {
			Connection conn = getSession();
			PreparedStatement ps = conn.prepareStatement("select trsISN,trsSkillName from TreatmentSkill");
			ResultSet re = ps.executeQuery();
			dsThuThuat = new ArrayList<DanhMuc>();
			while (re.next()) {
				DanhMuc dr = new DanhMuc(
						re.getLong("trsISN"), 
						re.getString("trsSkillName"));
				dsThuThuat.add(dr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsThuThuat;
	}
	
}
