/**
 * 
 */
package com.jiuyi.doctor.yaofang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.front.ServerResult;

/**
 * 
 * @author xutaoyang
 *
 */
@RestController
public class YaofangController {

	private static final String CMD = "medicine_";
	private static final String CMD_LOAD_LIST = CMD + "list";
	private static final String CMD_LOAD_SEARCH = CMD + "search";
	private static final String CMD_LOAD_DETAIL = CMD + "detail";

	private @Autowired YaofangManager manager;

	@RequestMapping(CMD_LOAD_LIST)
	public ServerResult medicineList(@Param("page") int page, @Param("pageSize") int pageSize) {
		return manager.loadFormatMedicines(page, pageSize);
	}

	@RequestMapping(CMD_LOAD_SEARCH)
	public ServerResult search(@Param("key") String key) {
		return manager.search(key);
	}

	@RequestMapping(CMD_LOAD_DETAIL)
	public ServerResult medicineDetail(@Param("id") String id) {
		return manager.medicineDetail(id);
	}

}
