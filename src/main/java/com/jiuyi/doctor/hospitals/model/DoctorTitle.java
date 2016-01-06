package com.jiuyi.doctor.hospitals.model;

/**
 * @Author: xutaoyang @Date: 上午11:35:21
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public enum DoctorTitle {

	Professor(1, "主任医师"), DeputyChief(2, "副主任医师"), ZhuZhi(3, "主治医师"), YiShi(4, "普通医师"),
	DirectorPharmacist(5, "主任药师"), DeputyDirectorPharmacist(6, "副主任药师"), CompetentPharmacist(7, "主治药师"), Pharmacist(8, "普通药师"),
	PRIMARY(9, "医师")
	;

	public final int id;
	public final String name;

	private DoctorTitle(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static DoctorTitle getTitleById(int id) {
		for (DoctorTitle title : values()) {
			if (title.id == id) {
				return title;
			}
		}
		return null;
	}

	public static String getTitleNameById(int id) {
		for (DoctorTitle title : values()) {
			if (title.id == id) {
				return title.name;
			}
		}
		return "";
	}

	public static boolean checkId(Integer titleId) {
		return Professor.id <= titleId && titleId <= YiShi.id;
	}
}
