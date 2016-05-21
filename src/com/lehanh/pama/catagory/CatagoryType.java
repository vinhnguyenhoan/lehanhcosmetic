package com.lehanh.pama.catagory;

public enum CatagoryType {

	SERVICE(){
		public Catagory createCatalog(Long id) {
			return new ServiceCatagory(id, this);
		}
	},
	PROGNOSTIC(
			//SERVICE
			) {
		public Catagory createCatalog(Long id) {
			return new PrognosticCatagory(id, this);
		}
	},
	DIAGNOSE(
			//PROGNOSTIC
			//SERVICE
			){
		public Catagory createCatalog(Long id) {
			return new DiagnoseCatagory(id, this);
		}
	},
	SURGERY(
			//DIAGNOSE
			//SERVICE
			) {
		public Catagory createCatalog(Long id) {
			return new SurgeryCatagory(id, this);
		}
	},
	
	ADVICE,
	
	DR {
		public Catagory createCatalog(Long id) {
			return new DrCatagory(id, this);
		}
	},
	
	APPOINTMENT {
		public Catagory createCatalog(Long id) {
			return new AppointmentCatagory(id, this);
		}
	}, 
	SPIRIT_LEVEL {
		public Catagory createCatalog(Long id) {
			return new SpiritLevel(id, this);
		}
	},
	PRESCRIPTION {
		public Catagory createCatalog(Long id) {
			return new PrescriptionCatagory(id, this);
		}
	},
	DRUG {
		public Catagory createCatalog(Long id) {
			return new DrugCatagory(id, this);
		}
	},
	DRUG_USE {
		public Catagory createCatalog(Long id) {
			return new DrugInfo(id, this);
		}
	},
	DRUG_UNIT {
		public Catagory createCatalog(Long id) {
			return new DrugInfo(id, this);
		}
	},
	DRUG_SESSION_PER_DAY {
		public Catagory createCatalog(Long id) {
			return new DrugInfo(id, this);
		}
	},
	DRUG_NOTICE {
		public Catagory createCatalog(Long id) {
			return new DrugInfo(id, this);
		}
	},
	;
	
	/*private final CatagoryType parentCatagoryType;
	
	private CatagoryType() {
		this.parentCatagoryType = null;
	}
	
	private CatagoryType(CatagoryType parentCatagoryType) {
		this.parentCatagoryType = parentCatagoryType;
	}
	
	public CatagoryType getParentCatagoryType() {
		return this.parentCatagoryType;
	}*/

	public Catagory createCatalog(Long id) {
		return new Catagory(id, this);
	}
	
	public Catagory createCatalog(Long id, String name, String desc) {
		Catagory result = createCatalog(id);
		result.setName(name);
		result.setDesc(desc);
		return result;
	}
}