/**
 * 
 */
package com.jiuyi.doctor.yaofang.model;

import com.jiuyi.frame.annotations.Column;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * 药品规格
 * 
 * @author xutaoyang
 * 
 */
public class Format implements ISerializableObj {

	@Column("format_id")
	private String id;

	@Column("prod_format")
	private String format;

	@Column("prod_price")
	private double price;

	/** 库存 */
	@Column("prod_sku")
	private int stock;

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("formatId", this.id);
		res.put("formatName", this.format);
		res.put("price", this.price);
		res.put("stock", this.stock);
		return res;
	}

	public String getId() {
		return id;
	}

	public String getFormat() {
		return format;
	}

	public double getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}
