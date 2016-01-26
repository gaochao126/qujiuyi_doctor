package com.jiuyi.doctor.yaofang;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.yaofang.model.Format;
import com.jiuyi.doctor.yaofang.model.FormatMedicine;
import com.jiuyi.doctor.yaofang.model.Medicine;

@Service
public class YaofangService {

	private @Autowired YaofangManager manager;

	public List<Medicine> loadMedicines(List<String> ids) {
		return manager.loadMedcines(ids);
	}

	public Medicine loadMedicine(String id) {
		return manager.loadMedicine(id);
	}

	public Format loadMedicineFormat(String formatId) {
		return manager.loadMedicineFormat(formatId);
	}

	public List<FormatMedicine> loadFormatMeds(List<String> formatIds) {
		return manager.loadFormatMeds(formatIds);
	}

	public List<FormatMedicine> loadAllFormatMedicine() {
		return manager.loadAllFormatMedicine();
	}
}
