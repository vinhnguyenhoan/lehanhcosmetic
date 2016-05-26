package com.lehanh.pama.old.model;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.lehanh.pama.catagory.AppointmentCatagory;
import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.PrescriptionItem;
import com.lehanh.pama.catagory.ServiceCatagory;
import com.lehanh.pama.patientcase.AppointmentSchedule;
import com.lehanh.pama.patientcase.MedicalPersonalInfo;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.patientcase.PatientCaseEntity;
import com.lehanh.pama.patientcase.PatientCaseStatus;
import com.lehanh.pama.util.CommonUtils;
import com.lehanh.pama.util.DateUtils;

public class BenhNhan {
	public long id;
	public String ten;
	public Date ngaySinh;
	public int tuoi;
	public String gioi;
	public String diaChi;
	public String dienThoai;
	public String dienThoaiDiDong;
	public String ngheNghiep;
	public String anh;
	public List<LanKhamBenh> danhSachKham = new ArrayList<LanKhamBenh>();
	public BenhNhan(long id, String ten, Date ngaySinh, int tuoi, String gioi, String diaChi, String dienThoai, String dienThoaiDiDong,
			String ngheNghiep, String anh) {
		super();
		this.id = id;
		this.ten = ten;
		this.ngaySinh = ngaySinh;
		this.tuoi = tuoi;
		this.gioi = gioi;
		this.diaChi = diaChi;
		this.dienThoai = dienThoai;
		this.dienThoaiDiDong = dienThoaiDiDong;
		this.ngheNghiep = ngheNghiep;
		this.anh = anh;
	}
	public void addLanKhamBenh(LanKhamBenh lanKham) {
		danhSachKham.add(lanKham);
	}
	
	int startBA = 1;
	int startTK = 0;
	
	public Patient convertPa(TreeMap<String, ServiceCatagory> servicesBySur, Map<String, Catagory> mapSgName, Map<Long, Long> mapIdDr, List<AppointmentSchedule> scheToSave, AppointmentCatagory appointmentCatagory) throws ParseException {
		Patient result = new Patient();
		
		result.setId(id);
		result.setImageName(anh);
		result.setName(ten);
		result.setAddress(diaChi);
		result.setBirthDay(ngaySinh);
		result.setFermale(!"Nam".equals(gioi));
		if (StringUtils.isBlank(dienThoaiDiDong)) {
			dienThoaiDiDong = "090...";
		}
		result.setCellPhone(dienThoaiDiDong);
		result.setPhone(dienThoai);
		//result.setEmail();
		result.setCareer(ngheNghiep);
		
		// Note from doctor about this patient
		// from 1-4 patientLevel; 
		//result.setNote();
		
		MedicalPersonalInfo mInfo = result.getMedicalPersonalInfo();
		List<PatientCaseEntity> paL = mInfo.getPatientCases();
		
		TreeMap<Long, TreeMap<Long, LanKhamBenh>> sortedDSKham = new TreeMap<>();
		for (LanKhamBenh lkb : danhSachKham) {
			long lk = lkb.lanKham;
			long ltk = lkb.lanTaiKham;
			
			TreeMap<Long, LanKhamBenh> lkM = sortedDSKham.get(lk);
			if (lkM == null) {
				lkM = new TreeMap<>();
				sortedDSKham.put(lk, lkM);
			}
			
			lkM.put(ltk, lkb);
		}

		// Sort by lan kham, lan tai kham
		for (Entry<Long, TreeMap<Long, LanKhamBenh>> entry : sortedDSKham.entrySet()) {
			sortedDSKham.put(entry.getKey(), CommonUtils.sortMapByKey(entry.getValue(), true));
		}
		sortedDSKham = CommonUtils.sortMapByKey(sortedDSKham, true);
		
		//Date lastVisit = null;
		int i = 1;
		for (Entry<Long, TreeMap<Long, LanKhamBenh>> entry : sortedDSKham.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			PatientCaseEntity pa = new PatientCaseEntity();
			pa.setId(i++);
			paL.add(pa);

			List<PatientCaseEntity> reE = pa.getReExamInfo();
			int deteailId = 1;
			TreeMap<Long, LanKhamBenh> detailList = CommonUtils.sortMapByKey(entry.getValue(), true);
			for (Entry<Long, LanKhamBenh> entryDetail : detailList.entrySet()) {
				LanKhamBenh lkb = entryDetail.getValue();

				PatientCaseEntity detail = new PatientCaseEntity();
				reE.add(detail);
				
				detail.setId(deteailId++);
				detail.setOldId(lkb.id);
				
				// update data for medical info
				if (StringUtils.isBlank(mInfo.getMedicalHistory()) && !StringUtils.isBlank(lkb.benhSu)) {
					mInfo.setMedicalHistory(lkb.benhSu);
				}
				if (StringUtils.isBlank(mInfo.getAnamnesis()) && !StringUtils.isBlank(lkb.tienCan)) {
					mInfo.setAnamnesis(lkb.tienCan);
				}
				if (StringUtils.isBlank(mInfo.getSummary()) || (!StringUtils.isBlank(lkb.dienBienBenh) && mInfo.getSummary().length() < lkb.dienBienBenh.length())) {
					mInfo.setSummary(lkb.dienBienBenh);
				}

				java.util.Date ngayKham = null;
				try {
					ngayKham = DateUtils.sqlDateToutilDate(lkb.ngayKham);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				// update data for Root case
				if (pa.getOldId() < 0) {
					pa.setOldId(lkb.id);
					pa.setDate(ngayKham);
				}
				
				detail.setDate(ngayKham);
				if (lkb.bacSyKham != null) {
					detail.setDrId(mapIdDr.get(lkb.bacSyKham.id));
				}
				if (lkb.danhSachTrieuChung != null) {
					List<String> prognosticCatagoryNames = new LinkedList<>();
					for (DanhMuc dm : lkb.danhSachTrieuChung) {
						prognosticCatagoryNames.add(dm.ten);
					}
					detail.setPrognosticCatagoryNames(prognosticCatagoryNames);
				}
				detail.setPrognosticOther(lkb.trieuCungNgoaiDS);
				
				if (lkb.danhSachChuanDoan != null) {
					List<String> diagnoseCatagoryNames = new LinkedList<>();
					for (DanhMuc dm : lkb.danhSachChuanDoan) {
						diagnoseCatagoryNames.add(dm.ten);
					}
					detail.setDiagnoseCatagoryNames(diagnoseCatagoryNames);
				}
				detail.setDiagnoseOther(lkb.chuanDoanNgoaiDS);
				
				List<String> serviceNames = null;
				if (lkb.danhSachPhauThuat != null) {
					serviceNames = new LinkedList<>();
					List<String> surgeryNames = new LinkedList<>();
					for (PhauThuat pt : lkb.danhSachPhauThuat) {
						// TODO pt.taiKham;
						surgeryNames.add(pt.ten);
						ServiceCatagory service = servicesBySur.get(pt.ten);
						if (service != null && !serviceNames.contains(service.getName())) {
							serviceNames.add(service.getName());
						}
					}
					detail.setSurgeryCatagoryNames(surgeryNames);
				}
				detail.setStatusEnum(PatientCaseStatus.EXAM);
				if (serviceNames != null && !serviceNames.isEmpty()) {
					detail.setServiceNames(serviceNames);
				}
				detail.setSurgeryDate(DateUtils.sqlDateToutilDate(lkb.ngayPhauThuat));
				
				detail.setAdviceFromDr(lkb.loiKhuyen);
				detail.setSmallSurgery(lkb.thuThuat);
				
				java.util.Date ngayTaiKham = DateUtils.sqlDateToutilDate(lkb.ngayTaiKham);
				if (ngayTaiKham != null) {
					AppointmentSchedule app = new AppointmentSchedule();
					app.setPatientId(id);
					
					String paInfo = this.ten;
					if (!StringUtils.isEmpty(dienThoaiDiDong)) {
						paInfo += " SDT - " + dienThoaiDiDong; //$NON-NLS-1$
					}
					app.setPaInfo(paInfo);
					
					app.setAppointmentDate(ngayTaiKham);
					app.setAppointmentCatagory(appointmentCatagory);
					
					detail.setAppoSchedule(app);
					if (scheToSave != null && DateUtils.calculateDate(ngayTaiKham, GregorianCalendar.getInstance().getTime()) > 0) {
						scheToSave.add(app);
					}
				}
				
				if (lkb.danhSachHinhAnh != null) {
					TreeMap<String, TreeMap<String, TreeMap<String, Object>>> picInfos = new TreeMap<String, TreeMap<String, TreeMap<String, Object>>>();
					int idHA = 1;
					for (HinhAnh ha : lkb.danhSachHinhAnh) {
						// name: 4154-Nose Aug Silas_1__B.JPG, thumuc: Nose Aug Silas\4150-4159
						String thuMucAnh = ha.thuMucAnh; //-> sym, image name, "id":5.0,"u_date":"12/05/2016"
						String[] thuMucAnhS = thuMucAnh.split("\\\\");
						String sym = thuMucAnhS[0];
						TreeMap<String, TreeMap<String, Object>> bySym = picInfos.get(sym);
						if (bySym == null) {
							bySym = new TreeMap<>();
							picInfos.put(sym, bySym);
						}
						
						TreeMap<String, Object> haData = new TreeMap<>();
						haData.put("id", idHA++);
						java.util.Date ngayCapNhat = DateUtils.sqlDateToutilDate(ha.ngayCapNhat);
						haData.put("u_date", DateUtils.convertDateDataType(ngayCapNhat));
						
						bySym.put(ha.ten, haData);
					}
					detail.setPicInfos(picInfos);
				}

				if (lkb.danhSachChiTietToaThuoc != null) {
					List<PrescriptionItem> pL = new LinkedList<>();
					for (ChiTietToaThuoc cttt : lkb.danhSachChiTietToaThuoc) {
						PrescriptionItem item = new PrescriptionItem(cttt.cachSuDung, cttt.cu, cttt.donVi, cttt.donViSuDung, cttt.luuY, "",// TODO ten goc 
								cttt.soLanSuDungTrenNgay, cttt.soLuong, cttt.soLuongSuDungTrenLan, cttt.ten);
						pL.add(item);
					}
					detail.setPrescription(pL);
				}
			}
		}
//		result.setLastVisit();
//		result.setLastSurgery();
//		result.setNextAppointment();
		return result;
	}

}