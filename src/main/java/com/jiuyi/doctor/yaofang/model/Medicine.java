/**
 * 
 */
package com.jiuyi.doctor.yaofang.model;

import java.util.List;

import com.jiuyi.frame.annotations.Column;
import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @author xutaoyang
 * 
 *         大药房里面的药品信息
 */
public class Medicine implements ISerializableObj {

	@Column("prod_id")
	private String id;

	@Column("prod_name")
	private String name;

	@Column("prod_usage")
	private String usage;

	@Column("img_id")
	@ConfigPrefix("medicine.img.url")
	private String img;

	private List<MedicineImg> imgs;
	private List<Format> formats;

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("medicineId", id);
		res.put("medicineName", name);
		res.put("usage", usage);
		res.put("img", img);
		res.putObjects("formats", formats);
		return res;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUsage() {
		return usage;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public List<MedicineImg> getImgs() {
		return imgs;
	}

	public List<Format> getFormats() {
		return formats;
	}

	public void setImgs(List<MedicineImg> imgs) {
		this.imgs = imgs;
	}

	public void setFormats(List<Format> formats) {
		this.formats = formats;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "Medicine [id=" + id + ", name=" + name + ", usage=" + usage + "]";
	}

}
