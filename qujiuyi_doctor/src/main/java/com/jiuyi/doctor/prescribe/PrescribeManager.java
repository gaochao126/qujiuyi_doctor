package com.jiuyi.doctor.prescribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jiuyi.doctor.prescribe.model.DoctorPrescribe;
import com.jiuyi.doctor.prescribe.model.Prescribe;
import com.jiuyi.doctor.prescribe.model.PrescribeStatus;
import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.ManagerBase;
import com.jiuyi.frame.conf.DBConfig;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.FileUtil;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 下午2:20:39
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class PrescribeManager extends ManagerBase<Doctor, DoctorPrescribe> {

	@Autowired
	private PrescribeDao dao;

	@Autowired
	private DBConfig dbConfig;

	/** 获取配药服务信息 */
	protected ServerResult loadInfo(Doctor doctor) {
		DoctorPrescribe doctorPrescribe = loadInfoBase(doctor);
		return new ServerResult("status", doctorPrescribe.getStatus());
	}

	/** 开启配药服务 */
	protected ServerResult openService(Doctor doctor) {
		updateStatus(doctor, ServiceStatus.OPENED);
		return new ServerResult();
	}

	/** 关闭配药服务 */
	protected ServerResult closeService(Doctor doctor) {
		updateStatus(doctor, ServiceStatus.CLOSED);
		return new ServerResult();
	}

	/** 获取所有该医生的配药记录 */
	protected ServerResult loadAllPrescribe(Doctor doctor) {
		return new ServerResult("list", dao.loadAllPrescribe(doctor));
	}

	/** 获取所有该医生未处理的配药记录 */
	protected ServerResult loadUnhandlePrescribe(Doctor doctor, Integer page, Integer pageSize) {
		if (page < 1) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		int startRow = (page - 1) * pageSize;
		return new ServerResult("list", dao.loadUnhandlePrescribe(doctor, startRow, pageSize));
	}

	/** 获取所有该医生已接收的配药记录 */
	protected ServerResult loadAcceptedPrescribe(Doctor doctor) {
		return new ServerResult("list", dao.loadAcceptedPrescribe(doctor));
	}

	/** 已经处理的 */
	protected ServerResult loadHandledPrescribe(Doctor doctor, Integer page, Integer pageSize) {
		if (page < 1) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		int startRow = (page - 1) * pageSize;
		return new ServerResult("list", dao.loadFinishedPrescribe(doctor, startRow, pageSize));
	}

	/** 接收配药,写处方单文件，再修改数据库状态 */
	protected ServerResult acceptPrescribe(Doctor doctor, Integer id, MultipartFile prescriptionList) {
		if (prescriptionList == null) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		Prescribe prescribe = dao.loadPrescribeById(doctor, id);
		if (prescribe == null || prescribe.getDoctorId() != doctor.getId()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		} else if (prescribe.getStatus() != PrescribeStatus.UNHANDLE.ordinal()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		String presFileName = String.format("%s-%s.%s", StringUtil.getRandomStr(10), doctor.getId(), FileUtil.getSuffix(prescriptionList));
		FileUtil.writeFile(dbConfig.getConfig("doctor.preslist.path"), presFileName, prescriptionList);
		dao.acceptPrescribe(id, presFileName);
		return new ServerResult();
	}

	/** 获取配药申请的药品清单 */
	protected ServerResult loadPrescribeMedcines(Doctor doctor, Integer prescribeId) {
		/* 验证该配药申请是否是该医生的 */
		boolean checkRes = dao.checkDoctorPrescribeId(doctor, prescribeId);
		if (!checkRes) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		return new ServerResult("list", dao.loadPrescribeMedcines(prescribeId));
	}

	/** 拒绝配药 */
	protected ServerResult refusePrescribe(Doctor doctor, Integer id, String reason) {
		Prescribe prescribe = dao.loadPrescribeById(doctor, id);
		if (prescribe == null || prescribe.getDoctorId() != doctor.getId()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		} else if (prescribe.getStatus() != PrescribeStatus.UNHANDLE.ordinal()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		dao.refusePrescribe(id, reason);
		return new ServerResult();
	}

	@Override
	protected DoctorPrescribe constructInfo(Doctor doctor) {
		DoctorPrescribe doctorPrescribe = dao.loadDoctorPrescribe(doctor);
		return doctorPrescribe != null ? doctorPrescribe : new DoctorPrescribe(0, doctor.getId(), ServiceStatus.CLOSED.ordinal());
	}

	protected ServerResult deletePrescribes(Doctor doctor, Integer[] prescribeIds) {
		dao.deletePrescribes(doctor, prescribeIds);
		return new ServerResult();
	}

	/** update 医生配药服务状态 */
	private void updateStatus(Doctor doctor, ServiceStatus serviceStatus) {
		loadInfoBase(doctor).setStatus(serviceStatus.ordinal());
		dao.updateStatus(doctor, serviceStatus);
	}

	protected ServerResult loadById(Doctor doctor, Integer id) {
		Prescribe prescribe = dao.loadDetailPrescribeById(doctor, id);
		if (prescribe != null) {
			prescribe.setMedicines(dao.loadPrescribeMedcines(id));
		}
		return new ServerResult(prescribe);
	}
}
