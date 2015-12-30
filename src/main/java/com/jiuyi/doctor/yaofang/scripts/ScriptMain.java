/**
 * 
 */
package com.jiuyi.doctor.yaofang.scripts;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.jiuyi.doctor.yaofang.YaofangDao;
import com.jiuyi.doctor.yaofang.model.FormatMedicine;

/**
 * 
 * @author xutaoyang
 *
 */
@Service
public class ScriptMain {

	private @Autowired YaofangDao dao;

	public void updatePinYin() {
		List<FormatMedicine> medicines = dao.loadFormatMedicines(1, 100);
		List<Object[]> updateArgs = new ArrayList<>();
		for (FormatMedicine medicine : medicines) {
			String pinyin = PinyinHelper.convertToPinyinString(medicine.getName(), "", PinyinFormat.WITHOUT_TONE);
			String shortPinYin = PinyinHelper.getShortPinyin(medicine.getName());
			updateArgs.add(new Object[] { pinyin, shortPinYin, medicine.getMedId() });
		}
		dao.updatePinYin(updateArgs);
	}

}
