package com.jiuyi.doctor.user;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jiuyi.doctor.permission.Permission;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.doctor.user.model.DoctorStatus;
import com.jiuyi.doctor.user.model.FillDoctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;

@Controller
public class UserController extends ControllerBase {

	private static final String CMD = "user";
	private static final String PRE = CMD + "_";
	private static final String CMD_LOAD_INFO = PRE + "info"; // 获取信息
	private static final String CMD_LOAD_BY_NAME = PRE + "load_by_name"; // 获取信息
	private static final String CMD_LOGIN = PRE + "login"; // 登录
	private static final String CMD_LOGIN_V2 = PRE + "login_v2"; // 用手机验证码登录
	private static final String CMD_LOGOUT = PRE + "logout"; // 退出
	private static final String CMD_SALT = PRE + "salt"; // 获取salt
	private static final String CMD_PHONE_EXIST = PRE + "phone_exist"; // 电话是否已经存在
	private static final String CMD_DOCTOR_REG = PRE + "register";// 医生注册
	private static final String CMD_DOCTOR_MOD = PRE + "modify";// 医生修改信息
	private static final String CMD_DOCTOR_MOD_SINGLE = PRE + "mod";// 医生修改单个信息
	private static final String CMD_DOCTOR_FILL = PRE + "fill_info";// 医生完善信息
	private static final String CMD_MODIFY_PHONE = PRE + "modify_phone";
	private static final String CMD_MODIFY_PASSWORD = PRE + "modify_password";
	private static final String CMD_MODIFY_HEAD = PRE + "modify_head";
	private static final String CMD_LOAD_INFO_BY_ID = PRE + "info_by_id";
	private static final String CMD_UPLOAD_TITLE_CARD = PRE + "upload_title";

	@Autowired
	@Qualifier("UserService")
	UserService service;

	@Autowired
	@Qualifier("UserManager")
	UserManager manager;

	public UserController() throws Exception {
		super(CMD);
	}

	@RequestMapping(CMD_LOAD_INFO)
	@ResponseBody
	public ServerResult loadInfo(@TokenUser Doctor doctor) {
		ServerResult res = new ServerResult();
		res.putObject(doctor);
		return res;
	}

	@RequestMapping(CMD_LOAD_INFO_BY_ID)
	@ResponseBody
	public ServerResult loadInfoById(@TokenUser Doctor doctor, @Param("id") Integer doctorId) {
		return manager.loadInfoById(doctor, doctorId);
	}

	@RequestMapping(CMD_LOAD_BY_NAME)
	@ResponseBody
	public ServerResult loadInfoByName(@Param("name") String name) {
		return manager.loadInfoByName(name);
	}

	@RequestMapping(CMD_PHONE_EXIST)
	@ResponseBody
	public ServerResult handleCheckPhoneExist(@Param("phone") String phone) {
		ServerResult res = service.phoneExist(phone);
		return res;
	}

	@RequestMapping(CMD_DOCTOR_REG)
	@ResponseBody
	public ServerResult handleDocReg(@Param("phone") String phone, @Param("phone") String password, @Param("verifyCode") String verifyCode, @Param("deviceType") Integer deviceType) {
		ServerResult res = service.handleReg(phone, password, verifyCode, deviceType);
		return res;
	}

	@RequestMapping(CMD_SALT)
	@ResponseBody
	public ServerResult handleGenSalt(HttpServletRequest request) {
		return new ServerResult(ResultConst.URL_ERROR);
		// String refresh_token = service.handleGenRefreshToken();
		// String salt = service.handleGenSalt(refresh_token);
		// ServerResult res = new ServerResult();
		// res.putToken(refresh_token);
		// res.put("salt", salt);
		// return res;
	}

	@RequestMapping(CMD_LOGIN)
	@ResponseBody
	public ServerResult handleLogin(@Param("phone") String phone, @Param("password") String password, @Param("token") String refresh_token, @Param("deviceType") Integer deviceType) {
		return new ServerResult(ResultConst.URL_ERROR);
		// ServerResult res = service.handleLogin(phone, password,
		// refresh_token, channelId, deviceType);
		// return res;
	}

	@RequestMapping(CMD_LOGIN_V2)
	@ResponseBody
	public ServerResult handleLoginv2(@Param("phone") String phone, @Param("code") String code, @Param("deviceType") Integer deviceType) {
		ServerResult res = service.handleLoginV2(phone, code, deviceType);
		return res;
	}

	@RequestMapping(CMD_LOGOUT)
	@ResponseBody
	public ServerResult handleLogout(@TokenUser @Permission(DoctorStatus.NEED_AUTH) Doctor doctor) {
		if (doctor != null) {
			service.handleLogout(doctor);
		}
		return new ServerResult();
	}

	@RequestMapping(CMD_DOCTOR_FILL)
	@ResponseBody
	public ServerResult handleFillInfo(@TokenUser @Permission(DoctorStatus.NEED_AUTH) Doctor doctor, @Valid @ModelAttribute FillDoctor newDoctor, BindingResult br,
			@RequestParam(value = "head", required = false) MultipartFile head, @RequestParam("idCard") MultipartFile idCard,
			@RequestParam(value = "titleCard", required = false) MultipartFile titleCard, @RequestParam("licenseCard") MultipartFile licenseCard) {
		if (br.hasErrors()) {
			ServerResult res = new ServerResult(ResultConst.PARAM_ERROR);
			res.put("err", br.getAllErrors());
			return res;
		}
		ServerResult res = service.handleFillInfo(doctor, newDoctor, head, idCard, titleCard, licenseCard);
		return res;
	}

	@RequestMapping(CMD_DOCTOR_MOD)
	@ResponseBody
	public ServerResult handleModifyInfo(@TokenUser Doctor doctor, @Param("doctor") Doctor newDoctor) {
		ServerResult res = service.handleModifyInfo(doctor, newDoctor);
		return res;
	}

	@RequestMapping(CMD_DOCTOR_MOD_SINGLE)
	@ResponseBody
	public ServerResult handleModifySingleInfo(@TokenUser Doctor doctor, @Param("key") String key, @Param("value") String value) {
		ServerResult res = service.handleModifySingleInfo(doctor, key, value);
		return res;
	}

	@RequestMapping(CMD_MODIFY_PHONE)
	@ResponseBody
	public ServerResult handleModifyPhone(@TokenUser Doctor doctor, @Param("phone") String phone, @Param("code") String code) {
		ServerResult res = service.handleModifyPhone(doctor, phone, code);
		return res;
	}

	@RequestMapping(CMD_MODIFY_PASSWORD)
	@ResponseBody
	public ServerResult handleModifyPassword(@TokenUser Doctor doctor, @Param("code") String code, @Param("password") String newPassword) {
		ServerResult res = service.handleModifyPassword(doctor, code, newPassword);
		return res;
	}

	@RequestMapping(CMD_MODIFY_HEAD)
	@ResponseBody
	public ServerResult handleModifyHead(@TokenUser Doctor doctor, @RequestParam("head") MultipartFile head) {
		ServerResult res = manager.modifyHead(doctor, head);
		return res;
	}

	@RequestMapping(CMD_UPLOAD_TITLE_CARD)
	@ResponseBody
	public ServerResult uploadTitleCard(@TokenUser Doctor doctor, @RequestParam("title") MultipartFile titleCard) {
		ServerResult res = manager.uploadTitleCard(doctor, titleCard);
		return res;
	}

}
