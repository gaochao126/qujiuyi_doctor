package com.jiuyi.doctor.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jiuyi.doctor.account.AccountService;
import com.jiuyi.doctor.chatserver.ChatServerRequestEntity;
import com.jiuyi.doctor.chatserver.ChatServerService;
import com.jiuyi.doctor.clinic.ClinicService;
import com.jiuyi.doctor.consult.ConsultService;
import com.jiuyi.doctor.hospitals.DepartmentService;
import com.jiuyi.doctor.hospitals.HospitalService;
import com.jiuyi.doctor.praise.PraiseService;
import com.jiuyi.doctor.user.model.BeforeLogin;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.doctor.user.model.DoctorStatus;
import com.jiuyi.doctor.user.model.EditStatus;
import com.jiuyi.doctor.user.model.FillDoctor;
import com.jiuyi.doctor.user.update.UpdateSkill;
import com.jiuyi.doctor.user.update.UpdateUserInfo;
import com.jiuyi.frame.conf.DBConfig;
import com.jiuyi.frame.event.EventService;
import com.jiuyi.frame.event.events.EventLogout;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.jobs.JobContext;
import com.jiuyi.frame.jobs.JobService;
import com.jiuyi.frame.jobs.JobType;
import com.jiuyi.frame.util.FileUtil;
import com.jiuyi.frame.util.NumberUtil;
import com.jiuyi.frame.util.StringUtil;
import com.jiuyi.frame.zervice.user.IUserManager;

@Service("UserManager")
public class UserManager implements IUserManager {

	private int EXPIRE_TIME;
	private String HERD_FILE_PATH;
	private String ID_CARD_FILE_PATH;
	private String TITLE_CARD_FILE_PATH;
	private String LICENSE_CARD_FILE_PATH;
	private String file_name_format = "%s-%s.%s";

	@Autowired
	@Qualifier("UserDAO")
	UserDAO userDao;

	@Autowired
	DBConfig dbConfig;

	@Autowired
	ChatServerService chatServerService;

	@Autowired
	ClinicService clinicService;

	@Autowired
	AccountService accountService;

	@Autowired
	HospitalService hospitalService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	ConsultService consultService;

	@Autowired
	JobService jobService;

	@Autowired
	EventService eventService;

	@Autowired
	PraiseService praiseService;

	@Autowired
	QRCodeService qrCodeService;

	private Map<String, UpdateUserInfo> name_update = new HashMap<>();
	private ConcurrentHashMap<Integer, Doctor> id_doctor = new ConcurrentHashMap<Integer, Doctor>();
	private ConcurrentHashMap<String, Doctor> token_doctor = new ConcurrentHashMap<String, Doctor>();
	private ConcurrentHashMap<String, BeforeLogin> refreshToken_beforeLogin = new ConcurrentHashMap<String, BeforeLogin>();
	private ConcurrentHashMap<Integer, String> id_token = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		this.HERD_FILE_PATH = dbConfig.getConfig("doctor.headPath");
		this.ID_CARD_FILE_PATH = dbConfig.getConfig("doctor.idCardPath");
		this.TITLE_CARD_FILE_PATH = dbConfig.getConfig("doctor.titleCardPath");
		this.LICENSE_CARD_FILE_PATH = dbConfig.getConfig("doctor.licenseCardPath");
		this.EXPIRE_TIME = NumberUtil.parseInt(dbConfig.getConfig("doctor.expireTime")) * 24 * 60 * 60 * 1000;
		this.name_update.put("skill", new UpdateSkill("skill", userDao, 5000));
		this.name_update.put("experience", new UpdateSkill("experience", userDao, 5000));
		jobService.submitJob(new JobContext(JobType.SCHEDULED, new ClearRunnable(), 5, 5, TimeUnit.SECONDS));

		// 测试专用
		Doctor doctor = userDao.loadDoctorByPhone("18725850672");
		String access_token = "123456";
		putDoctor(access_token, doctor);
		doctor.setChannelId("5443637761324926995");
		doctor.setDeviceType(4);
		doctor.setAccess_token(access_token);
		chatServerService.onLogin(doctor);
	}

	public boolean containsDoctor(String token) {
		return this.token_doctor.containsKey(token);
	}

	@Override
	public Doctor getUserByToken(String token) {
		Doctor doctor = this.token_doctor.get(token);
		if (doctor == null) {
			doctor = userDao.loadDoctorByToken(token);
			// 从数据库load出来代表服务器已经重启过，同步一次到聊天服
			if (doctor != null) {
				putDoctor(token, doctor);
				chatServerService.onLogin(doctor);
			}
		}
		if (doctor != null) {
			doctor.setAccess_token(token);
		}
		return doctor;
	}

	public Doctor getDoctorById(int doctorId) {
		if (this.id_doctor.containsKey(doctorId)) {
			return this.id_doctor.get(doctorId);
		}
		Doctor doctor = userDao.loadDoctorById(doctorId);
		this.id_doctor.put(doctorId, doctor);
		return doctor;
	}

	protected ServerResult loadInfoById(Doctor doctor, Integer doctorId) {
		ServerResult res = new ServerResult(userDao.loadDoctorById(doctorId));
		res.put("praised", praiseService.praised(doctor, doctorId));
		return res;
	}

	public void updateAccessTime(String access_token) {
		Doctor doctor = token_doctor.get(access_token);
		if (doctor != null) {
			doctor.setLastAccess();
		}
	}

	/** 根据名字获取医生列表 */
	protected ServerResult loadInfoByName(String name) {
		return new ServerResult("list", userDao.loadInfoByName(name), true);
	}

	protected boolean phoneExsit(String phone) {
		return userDao.isPhoneExist(phone);
	}

	protected Doctor addDoctor(String phone, String password) {
		int doctorId = userDao.addDoctor(phone, password);
		String token = generateToken(doctorId);
		Doctor doctor = new Doctor(doctorId, phone);
		doctor.setAccess_token(token);
		putDoctor(token, doctor);

		/* 因为同一个手机号可能既注册了患者，又注册了医生，医生端用md5(md5(id))做username，md5（password）做密码 */
		/* 患者端用md5(phone)做username，md5（password） 做密码 */
		ChatServerRequestEntity request = new ChatServerRequestEntity("huanxinRegister");
		Map<String, Object> params = new HashMap<>();
		params.put("userName", StringUtil.md5Str(StringUtil.md5Str(String.valueOf(doctorId))));
		params.put("userPassword", StringUtil.md5Str(String.valueOf(doctorId)));
		request.putParam("params", params);
		chatServerService.postMsgAsyn(request);
		return doctor;
	}

	/** 这个是获取access token，每次访问都需要带上这个token */
	protected String generateToken(int doctorId) {
		return String.format("%s-%s", doctorId, UUID.randomUUID().toString());
	}

	/** 这个是获取refresh token，只有在登录之前需要，用来存放salt之类的 */
	protected String generateRefreshToken() {
		return UUID.randomUUID().toString();
	}

	protected String genSalt(String refresh_token) {
		String salt = StringUtil.getRandomStr(5);
		refreshToken_beforeLogin.putIfAbsent(refresh_token, new BeforeLogin());
		refreshToken_beforeLogin.get(refresh_token).setSalt(salt);
		return salt;
	}

	/** 用密码登录 */
	protected ServerResult handleLogin(String phone, String password, String refresh_token, Integer deviceType) {
		BeforeLogin beforeLogin = refreshToken_beforeLogin.get(refresh_token);
		if (beforeLogin == null || !beforeLogin.hasSalt()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		String passwdInDB = userDao.getPasswordByPhone(phone);
		if (StringUtil.isNullOrEmpty(passwdInDB)) {
			return new ServerResult(ResultConst.HAVE_NOT_SET_PASSWORD);
		}
		String salt = beforeLogin.resetSalt();
		String afterMD5 = StringUtil.md5Str(passwdInDB + salt);
		if (!afterMD5.equals(password)) {
			return new ServerResult(ResultConst.PASSWORD_ERR);
		}
		refreshToken_beforeLogin.remove(refresh_token);
		return doctorLoginByPhone(phone, deviceType);
	}

	/** 用手机验证码登录 */
	protected ServerResult handleLoginV2(String phone, Integer deviceType) {
		/**** 如果注册过直接登录，否则新注册一个号码 ****/
		boolean hasRegistered = userDao.isPhoneExist(phone);
		if (hasRegistered) {
			return doctorLoginByPhone(phone, deviceType);
		}
		Doctor doctor = addDoctor(phone, "");
		ServerResult res = new ServerResult(ResultConst.NEW_USER);
		res.putToken(doctor.getAccess_token());
		putEasemobInfo(doctor, res);
		return res;
	}

	/** 手机短息验证码登录 */
	protected ServerResult doctorLoginByPhone(String phone, Integer deviceType) {
		Doctor doctor = userDao.loadDoctorByPhone(phone);
		String access_token = generateToken(doctor.getId());
		putDoctor(access_token, doctor);
		doctor.setDeviceType(deviceType);
		doctor.setAccess_token(access_token);
		// 登录信息同步到聊天服务器
		userDao.setToken(doctor);
		chatServerService.onLogin(doctor);
		qrCodeService.tryGenQRCode(doctor);
		ServerResult res = new ServerResult();
		res.putToken(access_token);
		res.putObject(doctor);
		if (doctor.getStatus() == DoctorStatus.VERIFY_FAIL.ordinal()) {
			res.put("authFailReason", userDao.getVerifyFailDesc(doctor));
		}
		res.putObjects("chatList", consultService.loadUnFinishConsult(doctor));
		putEasemobInfo(doctor, res);
		return res;
	}

	protected void handleLogout(Doctor doctor) {
		eventService.dispatchEvent(new EventLogout(doctor));
		// 登出信息同步到聊天服务器
		chatServerService.onLogout(doctor);
		int doctorId = doctor.getId();
		String token = doctor.getAccess_token();
		id_doctor.remove(doctorId);
		token_doctor.remove(token);
		userDao.removeToken(doctor);
	}

	@Transactional(rollbackFor = Exception.class)
	protected ServerResult handleFillInfo(Doctor doctor, FillDoctor newDoctor, MultipartFile head, MultipartFile idCard, MultipartFile titleCard, MultipartFile licenseCard) {
		if (!departmentService.checkId(newDoctor.getDepartmentId()) || !hospitalService.checkId(newDoctor.getHospitalId())) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		Integer offlineId = newDoctor.getOfflineId();
		if (offlineId != null && 0 != offlineId) {
			/* copy线下医生的特长和执业经历 */
			Doctor offlineDoctor = userDao.loadOfflineDoctor(offlineId);
			if (offlineDoctor != null) {
				newDoctor.setSkill(offlineDoctor.getSkill());
				newDoctor.setExperience(offlineDoctor.getExperience());
			}
		}
		String headFileName = String.format(file_name_format, StringUtil.getRandomStr(10), doctor.getId(), FileUtil.getSuffix(head));
		String idFileName = String.format(file_name_format, StringUtil.getRandomStr(10), doctor.getId(), FileUtil.getSuffix(idCard));
		String titleFileName = String.format(file_name_format, StringUtil.getRandomStr(10), doctor.getId(), FileUtil.getSuffix(titleCard));
		String licenseFileName = String.format(file_name_format, StringUtil.getRandomStr(10), doctor.getId(), FileUtil.getSuffix(licenseCard));
		FileUtil.writeFile(HERD_FILE_PATH, headFileName, head);
		FileUtil.writeFile(ID_CARD_FILE_PATH, idFileName, idCard);
		FileUtil.writeFile(TITLE_CARD_FILE_PATH, titleFileName, titleCard);
		FileUtil.writeFile(LICENSE_CARD_FILE_PATH, licenseFileName, licenseCard);
		newDoctor.setIdCardPath(idFileName);
		newDoctor.setHeadPath(headFileName);
		newDoctor.setTitleCardPath(titleFileName);
		newDoctor.setLicenseCardPath(licenseFileName);
		Doctor afterFill = userDao.fillDoctor(doctor, newDoctor);
		/* 插入认证信息 */
		newDoctor.setType(0);
		userDao.insertAuth(doctor, newDoctor);
		afterFill.copyInfo(doctor);
		putDoctor(doctor.getAccess_token(), afterFill);
		// 默认开一个诊所和账户
		clinicService.openClinic(afterFill);
		accountService.openAccount(afterFill);
		consultService.openDefaultConsult(afterFill);
		return new ServerResult();
	}

	protected ServerResult handleModifyInfo(Doctor doctor, Doctor newDoctor) {
		newDoctor.setId(doctor.getId());
		Doctor afterMod = userDao.modifyDoctor(newDoctor);
		afterMod.copyInfo(doctor);
		putDoctor(doctor.getAccess_token(), afterMod);
		return new ServerResult();
	}

	protected ServerResult handleModifySingleInfo(Doctor doctor, String key, String value) {
		UpdateUserInfo updateImpl = this.name_update.get(key);
		if (updateImpl == null) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		ResultConst rc = updateImpl.check(doctor, value);
		if (!rc.isSuccess()) {
			return new ServerResult(rc);
		}
		Doctor afterMod = updateImpl.updateCol(doctor, value);
		afterMod.copyInfo(doctor);
		putDoctor(doctor.getAccess_token(), afterMod);
		return new ServerResult();
	}

	protected void modifyPhone(Doctor doctor, String phone) {
		userDao.modifyPhone(doctor, phone);
		doctor.setPhone(phone);
	}

	protected void modifyPassword(Doctor doctor, String newPassword) {
		userDao.modifyPassword(doctor, newPassword);
	}

	/**
	 * 修改头像
	 * 
	 * @param doctor
	 * @param head
	 * @return
	 */
	protected ServerResult modifyHead(Doctor doctor, MultipartFile head) {
		if (head == null) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		String headFileName = String.format(file_name_format, StringUtil.getRandomStr(10), doctor.getId(), FileUtil.getSuffix(head));
		FileUtil.writeFile(HERD_FILE_PATH, headFileName, head);
		FillDoctor authDoctor = new FillDoctor();
		authDoctor.setDoctorId(doctor.getId());
		authDoctor.setType(1);
		authDoctor.setName(doctor.getName());
		authDoctor.setHeadPath(headFileName);
		authDoctor.setHospitalId(doctor.getHospitalId());
		authDoctor.setDepartmentId(doctor.getDepartmentId());
		authDoctor.setOfficePhone(doctor.getOfficePhone());
		authDoctor.setIdCardPath(doctor.getIdCardPath());
		authDoctor.setTitleCardPath(doctor.getTitleCardPath());
		authDoctor.setLicenseCardPath(doctor.getLicenseCardPath());
		/* 需要审核 */
		userDao.insertAuth(doctor, authDoctor);
		userDao.updateEditStatus(doctor, EditStatus.EDITED);
		return new ServerResult();
	}

	/** 把建立环信连接的用户密码返回给客户端，客户端用username和password与环信服务器建立连接 */
	private void putEasemobInfo(Doctor doctor, ServerResult res) {
		Integer doctorId = doctor.getId();
		res.put("easemobusername", StringUtil.md5Str(StringUtil.md5Str(String.valueOf(doctorId))));
		res.put("easemobpassword", StringUtil.md5Str(String.valueOf(doctorId)));
	}

	private void putDoctor(String token, Doctor doctor) {
		String oldToken = id_token.get(doctor.getId());
		if (oldToken != null) {
			token_doctor.remove(oldToken);
		}
		token_doctor.put(token, doctor);
		id_doctor.put(doctor.getId(), doctor);
		id_token.put(doctor.getId(), token);
	}

	class ClearRunnable implements Runnable {
		@Override
		public void run() {
			try {
				long now = System.currentTimeMillis();
				for (Iterator<Doctor> iter = token_doctor.values().iterator(); iter.hasNext();) {
					Doctor doctor = iter.next();
					if (doctor.isExpire(now, EXPIRE_TIME)) {
						if (Loggers.isDebugEnabled()) {
							Loggers.debugf("user:<<%s>> expired", doctor.getId());
						}
						handleLogout(doctor);
					}
				}
			} catch (Exception e) {
				Loggers.err("<<UserManger.ClearRunnable>> run err", e);
			}
		}
	}

}
