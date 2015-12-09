package com.jiuyi.doctor.patients.v2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午3:06:03
 *
 * @Description 第二版本--患者相关
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class PatientControllerV2 extends ControllerBase {

	private static final String CMD = "patient_v2_";
	private static final String CMD_LOAD_ALL = CMD + "all";// 患者首页

	private static final String CMD_LOAD_TAGS = CMD + "tags";// 所有标签
	private static final String CMD_ADD_TAG = CMD + "add_tag";// 添加标签
	private static final String CMD_SAVE_TAG = CMD + "save_tag";// 保存标签
	private static final String CMD_DELETE_TAG = CMD + "del_tag";// 删除标签
	private static final String CMD_SET_TAG = CMD + "set_tag";// 设置患者的标签
	private static final String CMD_LOAD_PATIENT_BY_TAG = CMD + "load_by_tag";// 获取标签下的患者

	private static final String CMD_UNFAMILIAR = CMD + "unfamiliar";// 陌生患者
	private static final String CMD_BLACKLIST = CMD + "blacklist";// 陌生患者
	private static final String CMD_MOVE_IN_CONTACTS = CMD + "move_in_contact";// 加入常用联系人
	private static final String CMD_MOVE_IN_BLACKLIST = CMD + "move_in_blacklist";// 加黑名单
	private static final String CMD_MOVE_IN_UNFAMILIAR = CMD + "move_in_unfamiliar";// 加入陌生联系人
	private static final String CMD_REMOVE_BLACKLIST = CMD + "remove_blacklist";// 删除黑名单，变为没有任何关系

	private static final String CMD_REMARK = CMD + "remark";// 备注名
	private static final String CMD_NOTE = CMD + "note";// 备注

	private static final String CMD_PATIENT_DETAIL = CMD + "detail";// 患者详细信息

	@Autowired
	private PatientManagerV2 manager;

	@Autowired
	private TagManager tagManager;

	@RequestMapping(CMD_LOAD_ALL)
	public ServerResult loadAll(@TokenUser Doctor doctor) {
		return manager.loadAll(doctor);
	}

	@RequestMapping(CMD_LOAD_TAGS)
	public ServerResult loadTags(@TokenUser Doctor doctor) {
		return tagManager.loadTags(doctor);
	}

	@RequestMapping(CMD_ADD_TAG)
	public ServerResult addTag(@TokenUser Doctor doctor, @Param("name") String name) {
		return tagManager.addTag(doctor, name);
	}

	@RequestMapping(CMD_SAVE_TAG)
	public ServerResult saveTag(@TokenUser Doctor doctor, @Param("tagId") Integer tagId, @Param("name") String name, @Param("patientIds") List<Integer> patientIds) {
		return tagManager.saveTag(doctor, tagId, name, patientIds);
	}

	@RequestMapping(CMD_DELETE_TAG)
	public ServerResult deleteTag(@TokenUser Doctor doctor, @Param("tagId") Integer tagId) {
		return tagManager.deleteTag(doctor, tagId);
	}

	@RequestMapping(CMD_SET_TAG)
	public ServerResult setPatientTags(@TokenUser Doctor doctor, @Param("patientId") Integer patientId, @Param("tagIds") List<Integer> tagIds) {
		return tagManager.setPatientTags(doctor, patientId, tagIds);
	}

	@RequestMapping(CMD_LOAD_PATIENT_BY_TAG)
	public ServerResult loadPatientsByTag(@TokenUser Doctor doctor, @Param("tagId") Integer tagId) {
		return tagManager.loadPatientsByTag(doctor, tagId);
	}

	@RequestMapping(CMD_UNFAMILIAR)
	public ServerResult loadUnfamiliarPatients(@TokenUser Doctor doctor) {
		return manager.loadUnfamiliarPatients(doctor);
	}

	@RequestMapping(CMD_BLACKLIST)
	public ServerResult loadBlacklistPatients(@TokenUser Doctor doctor) {
		return manager.loadBlacklistPatients(doctor);
	}

	@RequestMapping(CMD_MOVE_IN_CONTACTS)
	public ServerResult moveInContacts(@TokenUser Doctor doctor, @Param("patientId") Integer patientId) {
		return manager.moveInContacts(doctor, patientId);
	}

	@RequestMapping(CMD_REMARK)
	public ServerResult ramark(@TokenUser Doctor doctor, @Param("patientId") Integer patientId, @Param("remark") String remark) {
		return manager.remark(doctor, patientId, remark);
	}

	@RequestMapping(CMD_NOTE)
	public ServerResult note(@TokenUser Doctor doctor, @Param("patientId") Integer patientId, @Param("note") String note) {
		return manager.note(doctor, patientId, note);
	}

	@RequestMapping(CMD_PATIENT_DETAIL)
	public ServerResult patientDetailInfo(@TokenUser Doctor doctor, @Param("patientId") Integer patientId) {
		return manager.patientDetailInfo(doctor, patientId);
	}

	@RequestMapping(CMD_MOVE_IN_BLACKLIST)
	public ServerResult moveInBlacklist(@TokenUser Doctor doctor, @Param("patientId") Integer patientId) {
		return manager.moveInBlacklist(doctor, patientId);
	}

	@RequestMapping(CMD_REMOVE_BLACKLIST)
	public ServerResult removeBlacklist(@TokenUser Doctor doctor, @Param("patientId") Integer patientId) {
		return manager.removeBlacklist(doctor, patientId);
	}

	@RequestMapping(CMD_MOVE_IN_UNFAMILIAR)
	public ServerResult moveInUnfamiliar(@TokenUser Doctor doctor, @Param("patientId") Integer patientId) {
		return manager.moveInUnfamiliar(doctor, patientId);
	}

}
