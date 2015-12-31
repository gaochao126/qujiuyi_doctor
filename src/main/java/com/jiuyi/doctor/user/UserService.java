package com.jiuyi.doctor.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jiuyi.doctor.base.FileUploadCheckService;
import com.jiuyi.doctor.smsverify.SmsVerifyService;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.doctor.user.model.DoctorStatus;
import com.jiuyi.doctor.user.model.FillDoctor;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.TextEncryptor;
import com.jiuyi.frame.util.StringUtil;

@Service("UserService")
public class UserService {

	@Autowired
	@Qualifier("UserDAO")
	UserDAO userDAO;

	@Autowired
	@Qualifier("basicEncryptor")
	TextEncryptor encryptor;

	@Autowired
	@Qualifier("UserManager")
	UserManager userManager;

	@Autowired
	SmsVerifyService smsVerifyService;

	@Autowired
	FileUploadCheckService fileCheck;

	public Doctor getDoctorById(int doctorId) {
		return userManager.getDoctorById(doctorId);
	}

	/** 检测电话号码是否已经注册过 */
	protected ServerResult phoneExist(String phone) {
		boolean exist = userManager.phoneExsit(phone);
		ServerResult res = new ServerResult();
		res.put("phoneExist", exist);
		return res;

	}

	/** 医生注册 */
	protected ServerResult handleReg(String phone, String password, String verifyCode, Integer deviceType) {
		if (deviceType != 3 && deviceType != 4) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		ServerResult checkCodeRes = smsVerifyService.checkCode(phone, verifyCode);
		if (!checkCodeRes.isSuccess()) {
			return checkCodeRes;
		}
		if (userManager.phoneExsit(phone)) {
			return new ServerResult(ResultConst.PHONE_NUM_EXIST);
		}
		Doctor doctor = userManager.addDoctor(phone, password);
		ServerResult res = new ServerResult();
		res.putToken(doctor.getAccess_token());
		return res;
	}

	/** 获取随机数字，用于在登录密码之上做MD5 */
	protected String handleGenSalt(String refresh_token) {
		return userManager.genSalt(refresh_token);
	}

	/** 在登录之前的token */
	protected String handleGenRefreshToken() {
		return userManager.generateRefreshToken();
	}

	/** 登录过程 */
	protected ServerResult handleLogin(String phone, String password, String refresh_token, Integer deviceType) {
		if (StringUtil.isNullOrEmpty(phone) || StringUtil.isNullOrEmpty(password)) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		return userManager.handleLogin(phone, password, refresh_token, deviceType);
	}

	/** 用验证码登录 */
	protected ServerResult handleLoginV2(String phone, String code, Integer deviceType) {
		if (deviceType != 3 && deviceType != 4) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		ServerResult checkCodeRes = smsVerifyService.checkCode(phone, code);
		if (!checkCodeRes.isSuccess()) {
			return checkCodeRes;
		}
		return userManager.handleLoginV2(phone, deviceType);
	}

	/** 登出过程 */
	protected void handleLogout(Doctor doctor) {
		userManager.handleLogout(doctor);
	}

	/** 注册完后完善个人信息 */
	protected ServerResult handleFillInfo(Doctor doctor, FillDoctor newDoctor, MultipartFile head, MultipartFile idCard, MultipartFile titleCard, MultipartFile licenseCard) {
		if (doctor.getStatus() != DoctorStatus.NEED_AUTH.id) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		if (isFileNullOrEmpty(head) || isFileNullOrEmpty(idCard) || isFileNullOrEmpty(titleCard) || isFileNullOrEmpty(licenseCard)) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		ServerResult fileCheckRes = fileCheck.securityCheck(head, idCard, titleCard, licenseCard);
		if (!fileCheckRes.isSuccess()) {
			return fileCheckRes;
		}
		return userManager.handleFillInfo(doctor, newDoctor, head, idCard, titleCard, licenseCard);
	}

	/** 修改个人信息 */
	protected ServerResult handleModifyInfo(Doctor doctor, Doctor newDoctor) {
		return userManager.handleModifyInfo(doctor, newDoctor);
	}

	/** 修改医生的单项信息 */
	protected ServerResult handleModifySingleInfo(Doctor doctor, String key, String value) {
		return userManager.handleModifySingleInfo(doctor, key, value);
	}

	/** 修改电话号码 */
	protected ServerResult handleModifyPhone(Doctor doctor, String phone, String code) {
		ServerResult checkCodeRes = smsVerifyService.checkCode(phone, code);
		if (!checkCodeRes.isSuccess()) {
			return checkCodeRes;
		}
		userManager.modifyPhone(doctor, phone);
		return new ServerResult();
	}

	/** 修改密码 */
	protected ServerResult handleModifyPassword(Doctor doctor, String code, String newPassword) {
		ServerResult checkCodeRes = smsVerifyService.checkCode(doctor.getPhone(), code);
		if (!checkCodeRes.isSuccess()) {
			return checkCodeRes;
		}
		userManager.modifyPassword(doctor, newPassword);
		return new ServerResult();
	}

	private boolean isFileNullOrEmpty(MultipartFile file) {
		return file == null || file.isEmpty();
	}

	/**
	 * 按需更新token
	 * 
	 * @param doctor
	 */
	public void updateToken(Doctor doctor) {
		userManager.updateToken(doctor);
	}

}
