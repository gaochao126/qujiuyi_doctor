package com.jiuyi.doctor.yaofang;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.yaofang.model.Format;
import com.jiuyi.doctor.yaofang.model.FormatMedicine;
import com.jiuyi.doctor.yaofang.model.Medicine;
import com.jiuyi.doctor.yaofang.model.MedicineImg;

@Repository
public class YaofangDao extends YaofangDB {

	//@formatter:off
	private static final String SELECT_MEDICINE = "SELECT * FROM `Products` WHERE `prod_id`=(:id)";
	private static final String SELECT_BATCH_MEDICINES = "SELECT * FROM `Products` WHERE `prod_id` IN (:ids)";
	private static final String SELECT_PAGE_MEDICINES = "SELECT * FROM `Products` WHERE `prod_id` LIMIT ?,?";
	private static final String SEARCH_MEDICINE = "SELECT * FROM `Products` WHERE LOWER(`prod_name`) LIKE :key;";

	private static final String SELECT_MEDICINE_IMGS = "SELECT * FROM `Img` WHERE `prod_id`=:id";
	private static final String SELECT_MEDICINE_FORMATS = "SELECT * FROM `Formats` WHERE `prod_id`=:id";
	private static final String SELECT_MEDICINE_FORMAT = "SELECT * FROM `Formats` WHERE `format_id`=:id";

	private static final String SELECT_FORMAT_MEDS = "SELECT f.*,m.prod_id,m.prod_name,m.prod_usage,m.img_id,m.prod_pinyin,m.prod_firstABC,s.`shape_name` "
			+ "FROM `Formats` f,`Products` m "
			+ "JOIN `Shape` s ON s.`shape_id`=m.`shape_id` "
			+ "WHERE m.prod_id=f.prod_id AND f.`format_id` IN (:ids)";
	
	private static final String SELECT_ALL_FORMAT_MEDS = "SELECT f.*,m.prod_name,m.prod_usage,m.img_id,s.`shape_name` " + "FROM `Formats` f,`Products` m "
			+ "JOIN `Shape` s ON s.`shape_id`=m.`shape_id` "
			+ "WHERE m.prod_id=f.prod_id ORDER BY f.`prod_sku` DESC ";

	private static final String SELECT_PAGE_FORMAT_MEDS = SELECT_ALL_FORMAT_MEDS + " LIMIT ?,?";

	private static final String SEARCH_FORMAT_MEDS = "SELECT f.*,m.prod_name,m.prod_usage,m.img_id "
			+ "FROM `Formats` f,`Products` m "
			+ "WHERE m.prod_id=f.prod_id "
			+ "AND (LOWER(m.`prod_name`) LIKE :key "
			+ "OR LOWER(m.`prod_pinyin`) LIKE :key "
			+ "OR LOWER(m.`prod_firstABC`) LIKE :key) ";
	private static final String SELECT_FORMAT_MED = "SELECT f.*,m.prod_name,m.prod_usage,m.img_id,m.prod_chandi,s.`shape_name` "
			+ "FROM `Formats` f,`Products` m "//
			+ "JOIN `Shape` s ON s.`shape_id`=m.`shape_id` "
			+ "WHERE m.prod_id=f.prod_id AND f.format_id=?";
	//@formatter:on

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
	public List<Medicine> loadMedicines(int page, int pageSize) {
		return queryForList(SELECT_PAGE_MEDICINES, new Object[] { startIndex(page, pageSize), pageSize }, Medicine.class);
	}

	/**
	 * @param formatIds
	 * @return
	 */
	protected List<FormatMedicine> loadFormatMeds(List<String> formatIds) {
		return queryForList(SELECT_FORMAT_MEDS, new MapSqlParameterSource("ids", formatIds), FormatMedicine.class);
	}

	/**
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<FormatMedicine> loadFormatMedicines(int page, int pageSize) {
		return queryForList(SELECT_PAGE_FORMAT_MEDS, new Object[] { startIndex(page, pageSize), pageSize }, FormatMedicine.class);
	}

	/**
	 * @param key
	 * @return
	 */
	protected List<FormatMedicine> searchFormatMedicines(String key) {
		return queryForList(SEARCH_FORMAT_MEDS, new MapSqlParameterSource("key", "%" + key + "%"), FormatMedicine.class);
	}

	protected FormatMedicine loadFormatMedicine(String id) {
		return queryForObjectDefaultBuilder(SELECT_FORMAT_MED, new Object[] { id }, FormatMedicine.class);
	}

	/**
	 * @param updateArgs
	 */
	public void updatePinYin(List<Object[]> updateArgs) {
		String sql = "update `Products` set `prod_pinyin`=?,`prod_firstABC`=? where `prod_id`=?";
		jdbc.batchUpdate(sql, updateArgs);
	}

	/**
	 * @return
	 */
	protected List<FormatMedicine> loadAllFormatMedicine() {
		return queryForList(SELECT_ALL_FORMAT_MEDS, FormatMedicine.class);
	}

}
