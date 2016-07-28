import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.ui.patientcase.PrescriptionView;

public class Note {
	
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
