package com.jiuyi.doctor.yaofang;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.yaofang.model.Format;
import com.jiuyi.doctor.yaofang.model.FormatMedicine;
import com.jiuyi.doctor.yaofang.model.Medicine;
import com.jiuyi.frame.front.ServerResult;

@Service
public class YaofangManager {

	private @Autowired YaofangDao dao;

	/**
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public ServerResult loadMedicines(int page, int pageSize) {
		ServerResult res = new ServerResult();
		List<Medicine> medicines = dao.loadMedicines(page, pageSize);
		res.put("list", medicines);
		return res;
	}

	/**
	 * 把每一个规格当做独立的药品，同一种药，不同的规格返回不同的记录
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public ServerResult loadFormatMedicines(int page, int pageSize) {
		ServerResult res = new ServerResult();
		List<FormatMedicine> medicines = dao.loadFormatMedicines(page, pageSize);
		res.putObjects("list", medicines);
		return res;
	}

	/**
	 * @param key
	 * @return
	 */
	protected ServerResult search(String key) {
		ServerResult res = new ServerResult();
		List<FormatMedicine> medicines = dao.searchFormatMedicines(key);
		res.putObjects("list", medicines);
		return res;
	}

	/**
	 * @param id
	 * @return
	 */
	protected ServerResult medicineDetail(String id) {
		Medicine medicine = loadMedicine(id);
		ServerResult res = new ServerResult();
		res.putObject(medicine);
		return res;
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

	/**
	 * @param formatIds
	 * @return
	 */
	protected List<FormatMedicine> loadFormatMeds(List<String> formatIds) {
		return dao.loadFormatMeds(formatIds);
	}

}
