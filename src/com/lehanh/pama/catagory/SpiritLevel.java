package com.lehanh.pama.catagory;

import java.util.LinkedList;
import java.util.List;

public class SpiritLevel extends Catagory implements InternalCatagory {
	
	public SpiritLevel() {
		super(CatagoryType.SPIRIT_LEVEL);
	}

	SpiritLevel(Long id, CatagoryType catagoryType) {
		super(id, catagoryType);
	}

	@Override
	public List<Catagory> createCatagoryList() {
		List<Catagory> result = new LinkedList<Catagory>();
		long index = 1;
		result.add(this.getType().createCatalog(index++, "1", Messages.SpiritLevel_1)); //$NON-NLS-1$
		result.add(this.getType().createCatalog(index++, "2", Messages.SpiritLevel_3)); //$NON-NLS-1$
		result.add(this.getType().createCatalog(index++, "3", Messages.SpiritLevel_5)); //$NON-NLS-1$
		result.add(this.getType().createCatalog(index++, "4", Messages.SpiritLevel_7)); //$NON-NLS-1$
		
		return result;
	}
}
