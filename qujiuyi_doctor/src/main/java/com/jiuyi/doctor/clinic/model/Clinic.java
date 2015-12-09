package com.jiuyi.doctor.clinic.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.doctor.clinic.DoctorPosition;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午7:19:01
 *
 * @Description 诊所实体类
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Clinic implements ISerializableObj {

	private int id;
	private String name;
	private String slogan;
	private DoctorClinic leader;
	private List<DoctorClinic> doctors;

	public static final RowMapper<Clinic> builder = new RowMapper<Clinic>() {
		@Override
		public Clinic mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String slogan = rs.getString("slogan");
			return new Clinic(id, name, slogan);
		}
	};

	public Clinic(String name, String slogan) {
		this.name = name;
		this.slogan = slogan;
	}

	public Clinic(int id, String name, String slogan) {
		this.id = id;
		this.name = name;
		this.slogan = slogan;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public DoctorClinic getLeader() {
		return leader;
	}

	public List<DoctorClinic> getDoctors() {
		return doctors;
	}

	public void setLeader(DoctorClinic leader) {
		this.leader = leader;
	}

	public void setDoctors(List<DoctorClinic> doctors) {
		this.doctors = doctors;
		for (DoctorClinic dc : doctors) {
			if (DoctorPosition.LEADER.ordinal() == dc.getPosition()) {
				this.leader = dc;
				break;
			}
		}
	}

	public void addDoctor(DoctorClinic doctorClinic, DoctorPosition position) {
		if (DoctorPosition.LEADER.equals(position)) {
			this.leader = doctorClinic;
			return;
		}
		if (this.doctors == null) {
			this.doctors = new ArrayList<DoctorClinic>();
		}
		this.doctors.add(doctorClinic);
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("name", this.name);
		res.put("slogan", this.slogan);
		// res.putObjects("doctors", this.doctors);
		return res;
	}

	public boolean isLeader(Doctor doctor) {
		if (this.leader == null || doctor == null) {
			return false;
		}
		return leader.getDoctor().getId() == doctor.getId();
	}

}
