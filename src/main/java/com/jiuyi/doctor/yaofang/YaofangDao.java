package com.jiuyi.doctor.yaofang;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.yaofang.model.Format;
import com.jiuyi.doctor.yaofang.model.Medicine;
import com.jiuyi.doctor.yaofang.model.MedicineImg;

@Repository
public class YaofangDao extends YaofangDB {

	private static final String SELECT_MEDICINE = "SELECT * FROM `Products` WHERE `prod_id`=(:id)";
	private static final String SELECT_BATCH_MEDICINES = "SELECT * FROM `Products` WHERE `prod_id` IN (:ids)";
	private static final String SELECT_PAGE_MEDICINES = "SELECT * FROM `Products` WHERE `prod_id` LIMIT ?,?";
	private static final String SEARCH_MEDICINE = "SELECT * FROM `Products` WHERE LOWER(`prod_name`) LIKE :key;";

	private static final String SELECT_MEDICINE_IMGS = "SELECT * FROM `Img` WHERE `prod_id`=:id";
	private static final String SELECT_MEDICINE_FORMATS = "SELECT * FROM `Formats` WHERE `prod_id`=:id";
	private static final String SELECT_MEDICINE_FORMAT = "SELECT * FROM `Formats` WHERE `format_id`=:id";

	public List<Medicine> searchMedicine(String key) {
		return queryForList(SEARCH_MEDICINE, new MapSqlParameterSource("key", "%" + key.toLowerCase() + "%"), Medicine.class);
	}

	/**
	 * @param ids
	 * @return
	 */
	protected List<Medicine> loadMedicines(List<String> ids) {
		return queryForList(SELECT_BATCH_MEDICINES, new MapSqlParameterSource("ids", ids), Medicine.class);
	}

	/**
	 * @param id
	 * @return
	 */
	protected Medicine loadMedicine(String id) {
		return queryForObject(SELECT_MEDICINE, new MapSqlParameterSource("id", id), Medicine.class);
	}

	protected List<Format> loadMedicineFormats(String medicineId) {
		return queryForList(SELECT_MEDICINE_FORMATS, new MapSqlParameterSource("id", medicineId), Format.class);
	}

	protected List<MedicineImg> loadMedicineImgs(String medicineId) {
		return queryForList(SELECT_MEDICINE_IMGS, new MapSqlParameterSource("id", medicineId), MedicineImg.class);
	}

	/**
	 * @param formatId
	 * @return
	 */
	protected Format loadMedicineFormat(String formatId) {
		return queryForObject(SELECT_MEDICINE_FORMAT, new MapSqlParameterSource("id", formatId), Format.class);
	}

	/**
	 * @param page
	 * @param pageSize
	 * @return
	 */
	protected List<Medicine> loadMedicines(int page, int pageSize) {
		return queryForList(SELECT_PAGE_MEDICINES, new Object[] { startIndex(page, pageSize), pageSize }, Medicine.class);
	}

}
