/**
 * 
 */
package com.jiuyi.doctor.yaofang.model;

import java.util.List;

import com.jiuyi.frame.annotations.Column;

/**
 * @author xutaoyang
 * 
 *         大药房里面的药品信息
 */
public class Medicine {

	@Column("prod_id")
	private String id;

	@Column("prod_name")
	private String name;

	@Column("prod_usage")
	private String usage;

	private List<MedicineImg> imgs;
	private List<Format> formats;

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

	@Override
	public String toString() {
		return "Medicine [id=" + id + ", name=" + name + ", usage=" + usage + "]";
	}

}
