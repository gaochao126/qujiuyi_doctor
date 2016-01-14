/**
 * 
 */
package com.jiuyi.doctor.prescription.model;

import com.jiuyi.doctor.patients.model.Patient;
import com.jiuyi.frame.front.MapObject;

/**
 * 
 * @author xutaoyang
 *
 */
public class PatientPres extends Patient {

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jiuyi.doctor.patients.v2.model.Patient#serializeToMapObject()
	 */
	@Override
	public MapObject serializeToMapObject() {
		MapObject mo = super.serializeToMapObject();
		mo.put("count", count);
		return mo;
	}
}
