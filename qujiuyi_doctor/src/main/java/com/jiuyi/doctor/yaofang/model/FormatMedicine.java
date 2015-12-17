/**
 * 
 */
package com.jiuyi.doctor.yaofang.model;

import java.util.Arrays;

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
		/** 为了让前端少建一个对象。。。这里返回的json格式和medicine detail的格式一样，所以就有了下面你看的想吐槽的代码，（逃 */
		MapObject format = new MapObject();
		format.put("medicineId", this.medId);
		format.put("medicineName", this.name);
		format.put("usage", this.usage);
		format.put("img", this.img);
		res.putObjects("formats", Arrays.asList(format));
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
