/**
 * 
 */
package com.jiuyi.doctor.yaofang.model;

import com.jiuyi.frame.annotations.Column;
import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.front.MapObject;

/**
 * 带有药品信息的规格类
 * 
 * @author xutaoyang
 *
 */
public class FormatMedicine extends Format {

	@Column("prod_id")
	private String medId;

	@Column("prod_name")
	private String name;

	@Column("prod_usage")
	private String usage;

	@Column("img_id")
	@ConfigPrefix("medicine.img.url")
	private String img;

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = super.serializeToMapObject();
		res.put("medicineId", this.medId);
		res.put("medicineName", this.name);
		res.put("usage", this.usage);
		res.put("img", this.img);
		return res;

	}

	public String getMedId() {
		return medId;
	}

	public void setMedId(String medId) {
		this.medId = medId;
	}

	public String getName() {
		return name;
	}

	public String getUsage() {
		return usage;
	}

	public String getImg() {
		return img;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
