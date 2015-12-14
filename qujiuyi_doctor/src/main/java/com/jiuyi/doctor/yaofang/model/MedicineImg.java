/**
 * 
 */
package com.jiuyi.doctor.yaofang.model;

import com.jiuyi.frame.annotations.ConfigPrefix;

/**
 * 药品图片
 * 
 * @author xutaoyang
 *
 */
public class MedicineImg {

	@ConfigPrefix("medicine.img.url")
	private String img_src;
	private String img_type;

	public String getImg_src() {
		return img_src;
	}

	public String getImg_type() {
		return img_type;
	}

	public void setImg_src(String img_src) {
		this.img_src = img_src;
	}

	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}

}
