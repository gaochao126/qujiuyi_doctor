/**
 * 
 */
package com.jiuyi.doctor.yaofang.model;

import java.util.Arrays;

import com.jiuyi.frame.annotations.Column;
import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.StringUtil;

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
	private String instructions;

	@Column("img_id")
	@ConfigPrefix("medicine.img.url")
	private String img;

	@Column("prod_pinyin")
	private String pinyin;

	@Column("prod_firstABC")
	private String camel;

	@Column("shape_name")
	private String shapeName;

	@Column("prod_chandi")
	private String producer;

	@Override
	public MapObject serializeToMapObject() {
		/** 为了让前端少建一个对象。。。这里返回的json格式和medicine detail的格式一样，所以就有了下面你看的想吐槽的代码，（逃 */
		MapObject res = new MapObject();
		res.put("medicineId", this.medId);
		res.put("medicineName", this.name);
		res.put("instructions", this.instructions);
		res.put("img", this.img);
		res.put("shapeName", this.shapeName);
		res.put("producer", this.producer);
		res.putObjects("formats", Arrays.asList(super.serializeToMapObject()));
		return res;

	}

	public boolean like(String key) {
		return (!StringUtil.isNullOrEmpty(name) && name.contains(key)) || (!StringUtil.isNullOrEmpty(pinyin) && pinyin.contains(key)) || (!StringUtil.isNullOrEmpty(camel) && camel.contains(key));
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

	public String getImg() {
		return img;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getPinyin() {
		return pinyin;
	}

	public String getCamel() {
		return camel;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public void setCamel(String camel) {
		this.camel = camel;
	}

	public String getShapeName() {
		return shapeName;
	}

	public void setShapeName(String shapeName) {
		this.shapeName = shapeName;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

}
