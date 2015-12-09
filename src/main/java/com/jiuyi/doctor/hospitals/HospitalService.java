package com.jiuyi.doctor.hospitals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.hospitals.model.Catagory;
import com.jiuyi.doctor.hospitals.model.Hospital;
import com.jiuyi.frame.util.CollectionUtil;

/**
 * @Author: xutaoyang @Date: 下午4:25:28
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class HospitalService {

	@Autowired
	HospitalDao dao;

	private AtomicInteger currentMaxId = new AtomicInteger(0);
	private ConcurrentHashMap<Integer, Hospital> id_info = new ConcurrentHashMap<Integer, Hospital>();
	private ConcurrentHashMap<String, Hospital> name_info = new ConcurrentHashMap<String, Hospital>();
	private CopyOnWriteArrayList<Hospital> hospitals = new CopyOnWriteArrayList<>();

	private Map<Integer, List<Hospital>> catagoryId_hospitals = new HashMap<>();
	private List<Catagory> catagories = new ArrayList<>();

	@PostConstruct
	public void init() {
		this.currentMaxId = new AtomicInteger(dao.loadMaxId());
		ConcurrentHashMap<Integer, Hospital> temp_id_info = new ConcurrentHashMap<Integer, Hospital>();
		ConcurrentHashMap<String, Hospital> temp_name_info = new ConcurrentHashMap<String, Hospital>();
		CopyOnWriteArrayList<Hospital> temp_hospitals = new CopyOnWriteArrayList<>();
		List<Hospital> hospitals = dao.loadAll();
		if (!CollectionUtil.isNullOrEmpty(hospitals)) {
			for (Hospital hospital : hospitals) {
				temp_id_info.put(hospital.getId(), hospital);
				temp_name_info.put(hospital.getName(), hospital);
				temp_hospitals.add(hospital);
				put2Catagory(hospital);
			}
		}
		this.id_info = temp_id_info;
		this.name_info = temp_name_info;
		this.hospitals = temp_hospitals;
	}

	private void put2Catagory(Hospital hospital) {
		if (!catagoryId_hospitals.containsKey(hospital.getCatagoryId())) {
			this.catagoryId_hospitals.put(hospital.getCatagoryId(), new ArrayList<Hospital>());
			this.catagories.add(new Catagory(hospital.getCatagoryId(), hospital.getCatagoryName()));
		}
		catagoryId_hospitals.get(hospital.getCatagoryId()).add(hospital);
	}

	public int getIdByName(String name) {
		if (!this.name_info.containsKey(name)) {
			int newId = genId();
			Hospital hospital = new Hospital(newId, name);
			hospital = this.id_info.putIfAbsent(newId, hospital);
			hospital = this.name_info.putIfAbsent(name, hospital);
			dao.newHospital(hospital);
			return newId;
		}
		return this.name_info.get(name).getId();
	}

	public String getNameById(int id) {
		if (!this.id_info.containsKey(id)) {
			return "";
		}
		return this.id_info.get(id).getName();
	}

	public Integer genId() {
		return currentMaxId.incrementAndGet();
	}

	public List<Hospital> getAllHospitals() {
		return this.hospitals;
	}

	public boolean checkId(Integer hospitalId) {
		return this.id_info.containsKey(hospitalId);
	}

	protected List<Catagory> loadHospitalLevel1() {
		return this.catagories;
	}

	protected List<Hospital> loadHospitalLevel2(Integer level1Id) {
		return this.catagoryId_hospitals.get(level1Id);
	}
}
