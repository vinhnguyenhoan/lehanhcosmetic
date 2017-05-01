import java.util.TreeMap;

import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.ui.patientcase.PrescriptionView;

public class Note {
	
	// từ 2009 -> danh muc phau thuat cũ
	/* ?????
	 1. a:b:c ->b
	 if (st.contains(":")) {
								String[] ss = st.split(":");
								if (ss.length > 1) {
									st = ss[1].trim();
								} else {
									st = ss[0].trim();
								}
							}
							
	2.- a - b
	for (int i = 0; i < surs.length - 1; i++) { 

	 */
	
	
	
	// từ 2009 -> benh án ngoài thẩm mỹ
	
	//30/09/2016
	//Nguyen thi anh - 17163 -> chon benh an 1 -> 2 -> 1 -> error 
	
	// Klamentin 1g -> check ten gốc
	
	// 08/07/2016
	//Hút mỡ
	/*
Caused by: java.lang.NullPointerException
	at com.lehanh.pama.ui.util.CatagoryToUITextByDesc.showUI(CatagoryToUITextByDesc.java:10)
	at com.lehanh.pama.ui.util.UIControlUtils.initialCombo(UIControlUtils.java:44)
	at com.lehanh.pama.ui.patientcase.PatientCaseView$11.modifyText(PatientCaseView.java:716)

Caused by: org.eclipse.core.runtime.AssertionFailedException: null argument:
	at org.eclipse.core.runtime.Assert.isNotNull(Assert.java:85)
	at org.eclipse.core.runtime.Assert.isNotNull(Assert.java:73)
	at org.eclipse.jface.viewers.StructuredViewer.update(StructuredViewer.java:2092)
	at org.eclipse.jface.viewers.ColumnViewer.update(ColumnViewer.java:541)
	at com.lehanh.pama.ui.patientcase.PatientCaseCatagoryComboViewer.selectionChanged(PatientCaseCatagoryComboViewer.java:114)
	at com.lehanh.pama.ui.patientcase.PatientCaseView.selectServiceCatagory(PatientCaseView.java:851)
	at com.lehanh.pama.ui.patientcase.PatientCaseView.viewDetailCase(PatientCaseView.java:814)
	at com.lehanh.pama.ui.patientcase.ExamVersionComboViewer.selectionChanged(ExamVersionComboViewer.java:143)
	at com.lehanh.pama.ui.patientcase.ExamVersionComboViewer.inputChanged(ExamVersionComboViewer.java:176)
	 */
	
	public static void main(String[] args) {
		TreeMap<String, String> a = new TreeMap<>();
		a.put("Bơm mỡ dưới mắt", "1");
		a.put("Bơm mỡ dưới mắt2", "1");
		a.put("Bơm mỡ dưới mắt 3", "1");
		
		System.out.println(a.get("Bơm mỡ dưới mắt"));
	}
	
	/// benh cũ them benh án moi ko edit form đc
	
	/// xem benh cu roi tao moi benh nhan + benh an
	/*
	java.security.InvalidParameterException: Bệnh án gần nhất đang trống, sử dụng bệnh án này để thăm khám hay tư vấn !
	at com.lehanh.pama.patientcase.PatientCaseList.createRootCase(PatientCaseList.java:92)
	at com.lehanh.pama.ui.patientcase.PatientCaseView.newAction(PatientCaseView.java:780)
	at com.lehanh.pama.ui.patientcase.PatientCaseView.newCase(PatientCaseView.java:768)
	at com.lehanh.pama.ui.patientcase.PatientCaseView.access$2(PatientCaseView.java:766)
	at com.lehanh.pama.ui.patientcase.PatientCaseView$5.widgetSelected(PatientCaseView.java:448)
	 */
	
	// Missing Exernaltext class;
	PrescriptionView f1;
	Patient f2;
	
	// TODO prescription must auto view lasest patient case at default -> select patient
	PrescriptionView f3;
}
