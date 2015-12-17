/**
 * 
 */
package com.jiuyi.doctor.test.patient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.doctor.patients.v2.PatientServiceV2;
import com.jiuyi.doctor.patients.v2.model.Patient;

import junit.framework.TestCase;

/**
 * 
 * @author xutaoyang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/servlet-context.xml", "/root-context.xml" })
@Transactional
public class PatientTest extends TestCase {

	@Autowired
	PatientServiceV2 patientService;

	@Test
	public void testLoadPatient() {

		Patient p1 = patientService.loadPatient(2);
		Patient p2 = patientService.loadPatient(100);
		
		assertNotNull(p1);
		assertNull(p2);
	}

}
