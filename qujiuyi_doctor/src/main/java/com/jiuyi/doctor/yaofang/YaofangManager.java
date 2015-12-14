package com.jiuyi.doctor.yaofang;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.yaofang.model.Format;
import com.jiuyi.doctor.yaofang.model.Medicine;

@Service
public class YaofangManager {

	private @Autowired YaofangDao dao;

	@PostConstruct
	public void init() {
	}

	/**
	 * 搜索
	 * 
	 * @param ids
	 * @return
	 */
	protected List<Medicine> searchMedcines(String key) {
		return dao.searchMedicine(key);
	}

	/**
	 * 批量获取
	 * 
	 * @param ids
	 * @return
	 */
	protected List<Medicine> loadMedcines(List<String> ids) {
		return dao.loadMedicines(ids);
	}

	/**
	 * @param id
	 * @return
	 */
	protected Medicine loadMedicine(String id) {
		Medicine medicine = dao.loadMedicine(id);
		if (medicine != null) {
			medicine.setFormats(dao.loadMedicineFormats(id));
			medicine.setImgs(dao.loadMedicineImgs(id));
		}
		return medicine;
	}

	/**
	 * 规格信息
	 * 
	 * @param formatId
	 * @return
	 */
	protected Format loadMedicineFormat(String formatId) {
		return dao.loadMedicineFormat(formatId);
	}

}
