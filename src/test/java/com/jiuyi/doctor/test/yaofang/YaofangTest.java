/**
 * 
 */
package com.jiuyi.doctor.test.yaofang;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.doctor.yaofang.YaofangService;
import com.jiuyi.doctor.yaofang.model.Format;
import com.jiuyi.doctor.yaofang.model.Medicine;
import com.jiuyi.frame.util.JsonUtil;

import junit.framework.TestCase;

/**
 * @author xutaoyang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/servlet-context.xml", "/root-context.xml" })
@Transactional
public class YaofangTest extends TestCase {
	@Autowired
	YaofangService service;

	@Test
	public void testBatchSelect() {
		List<String> ids = Arrays.asList("14321997287694");
		List<Medicine> medicines = service.loadMedicines(ids);
		System.err.println(medicines);
		assertNotNull(medicines);
	}

	@Test
	public void testSelectDetail() {
		Medicine medicine = service.loadMedicine("143219972876947");
		System.err.println(JsonUtil.toJson(medicine));
	}

	@Test
	public void testLoadFormat() {
		Format f = service.loadMedicineFormat("219972876337");
		System.err.println(JsonUtil.toJson(f));
	}
}
