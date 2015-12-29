package com.jiuyi.doctor.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.hospitals.DepartmentService;
import com.jiuyi.doctor.hospitals.HospitalService;
import com.jiuyi.doctor.user.builder.DoctorBuilder;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.doctor.user.model.DoctorStatus;
import com.jiuyi.doctor.user.model.EditStatus;
import com.jiuyi.doctor.user.model.FillDoctor;
import com.jiuyi.doctor.user.model.RecognizeDoctor;
import com.jiuyi.doctor.user.model.RecognizeDoctorBuilder;
import com.jiuyi.frame.base.DbBase;

@Repository("UserDAO")
public class UserDAO extends DbBase {

	@Autowired
	HospitalService hospitalService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	DoctorBuilder doctorBuilder;

	@Autowired
	RecognizeDoctorBuilder recognizeDoctorBuilder;

	private static final String SELECT_BY_PHONE = "SELECT * FROM `t_doctor` where `phone`=?";
	private static final String SELECT_PHONE = "SELECT count(`id`) FROM `t_doctor` where `phone`=?";
	private static final String SELECT_PASSWORD = "SELECT `password` FROM `t_doctor` where `phone`=?";
	private static final String SELECT_BY_ID = "SELECT * FROM `t_doctor` WHERE `id`=?";
	private static final String SELECT_BY_NAME = "SELECT * FROM `t_hospital_doctor` WHERE `name`=?";
	private static final String SELECT_VERIFY_FAIL_DESC = "SELECT `description` FROM `t_doctor_auth` WHERE `doctorId`=? ORDER BY `id` DESC LIMIT 1";
	private static final String SELECT_HOSPITAL_DOCTOR = "SELECT * FROM `t_hospital_doctor` WHERE `id`=?";

	private static final String INSERT_DOCTOR = "INSERT INTO `t_doctor`(`phone`,`password`,`status`) VALUES(?,?,?)";
	private static final String INSERT_AUTH = "INSERT `t_doctor_auth`(`doctorId`,`name`,`hospitalId`,`departmentId`,`officePhone`,`head`,`idCardPath`,`titleCardPath`,`licenseCardPath`,`type`) VALUE(?,?,?,?,?,?,?,?,?,?)";
	private static final String FILL_DOCTOR = "UPDATE `t_doctor` SET `name`=?,`hospitalId`=?,`departmentId`=?,`officePhone`=?,`titleId`=?,`status`=?, `head`=?,`idCardPath`=?,`titleCardPath`=?,`licenseCardPath`=?,skill=?,experience=?,offlineId=? WHERE `id`=?";
	private static final String UPDATE_DOCTOR = "UPDATE `t_doctor` SET `name`=?,`hospitalId`=? WHERE `id`=?";
	private static final String UPDATE_PHONE = "UPDATE `t_doctor` SET `phone`=? WHERE `id`=?";
	private static final String UPDATE_PASSWORD = "UPDATE `t_doctor` SET `password`=? WHERE `id`=?";
	private static final String UPDATE_COL = "UPDATE `t_doctor` SET `#col#`=? WHERE `id`=?";
	private static final String UPDATE_HEAD = "UPDATE `t_doctor` SET `head`=? WHERE `id`=?";
	private static final String UPDATE_QRCODE = "UPDATE `t_doctor` SET `qrCodeImg`=? WHERE `id`=?";
	private static final String UPDATE_EIDT_STATUS = "UPDATE `t_doctor` SET `editStatus`=? WHERE `id`=?";

	public Doctor updateSingleCol(Doctor doctor, String dbField, Object value) {
		String sql = UPDATE_COL.replace("#col#", dbField);
		jdbc.update(sql, new Object[] { value, doctor.getId() });
		return loadDoctorById(doctor.getId());
	}

	protected boolean isPhoneExist(String phone) {
		return queryForInteger(SELECT_PHONE, new Object[] { phone }) > 0;
	}

	protected String getVerifyFailDesc(Doctor doctor) {
		return queryForString(SELECT_VERIFY_FAIL_DESC, doctor.getId());
	}

	protected Doctor modifyDoctor(Doctor doctor) {
		jdbc.update(UPDATE_DOCTOR, doctor.getName(), hospitalService.getIdByName(doctor.getHospital()), doctor.getId());
		return loadDoctorById(doctor.getId());
	}

	protected Doctor fillDoctor(Doctor doctor, FillDoctor fillDoctor) {
		/** 插入医生信息表 */
		jdbc.update(FILL_DOCTOR, fillDoctor.getName(), fillDoctor.getHospitalId(), fillDoctor.getDepartmentId(), fillDoctor.getOfficePhone(), fillDoctor.getTitleId(),
				DoctorStatus.UNDER_VERIFY.ordinal(), fillDoctor.getHeadPath(), fillDoctor.getIdCardPath(), fillDoctor.getTitleCardPath(), fillDoctor.getLicenseCardPath(), fillDoctor.getSkill(),
				fillDoctor.getExperience(), fillDoctor.getOfflineId(), doctor.getId());
		return loadDoctorById(doctor.getId());
	}

	protected void insertAuth(Doctor doctor, FillDoctor fillDoctor) {
		/** 插入认证信息表 */
		jdbc.update(INSERT_AUTH, doctor.getId(), fillDoctor.getName(), fillDoctor.getHospitalId(), fillDoctor.getDepartmentId(), fillDoctor.getOfficePhone(), fillDoctor.getHeadPath(),
				fillDoctor.getIdCardPath(), fillDoctor.getTitleCardPath(), fillDoctor.getLicenseCardPath(), fillDoctor.getType());
	}

	public int addDoctor(final String phone, final String password) {
		KeyHolder holder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_DOCTOR, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, phone);
				ps.setString(2, password);
				ps.setInt(3, DoctorStatus.NEED_AUTH.ordinal());
				return ps;
			}
		};
		jdbc.update(psc, holder);
		int id = holder.getKey().intValue();
		return id;
	}

	protected String getPasswordByPhone(String phone) {
		return queryForObject(SELECT_PASSWORD, new Object[] { phone }, String.class);
	}

	protected Doctor loadDoctorByPhone(String phone) {
		return queryForObject(SELECT_BY_PHONE, new Object[] { phone }, doctorBuilder);
	}

	protected List<RecognizeDoctor> loadInfoByName(String name) {
		return jdbc.query(SELECT_BY_NAME, new Object[] { name }, recognizeDoctorBuilder);
	}

	protected Doctor loadDoctorById(int id) {
		return queryForObject(SELECT_BY_ID, new Object[] { id }, doctorBuilder);
	}

	protected void modifyPhone(Doctor doctor, String phone) {
		jdbc.update(UPDATE_PHONE, new Object[] { phone, doctor.getId() });
	}

	protected void modifyPassword(Doctor doctor, String newPassword) {
		jdbc.update(UPDATE_PASSWORD, new Object[] { newPassword, doctor.getId() });
	}

	protected void updateHeadPath(Doctor doctor, String headFileName) {
		jdbc.update(UPDATE_HEAD, new Object[] { headFileName, doctor.getId() });
	}

	protected void updateQRCodeImg(Doctor doctor, String qrCode) {
		jdbc.update(UPDATE_QRCODE, new Object[] { qrCode, doctor.getId() });
	}

	protected void updateQRCodeImg(Integer doctorId, String qrCode) {
		jdbc.update(UPDATE_QRCODE, new Object[] { qrCode, doctorId });
	}

	protected List<Integer> doctorIds() {
		return jdbc.queryForList("select `id` from `t_doctor`", Integer.class);
	}

	protected Doctor loadOfflineDoctor(int offlineId) {
		return queryForObjectDefaultBuilder(SELECT_HOSPITAL_DOCTOR, new Object[] { offlineId }, Doctor.class);
	}

	protected void updateEditStatus(Doctor doctor, EditStatus editStatus) {
		jdbc.update(UPDATE_EIDT_STATUS, editStatus.ordinal(), doctor.getId());
	}
}
