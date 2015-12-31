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

import com.jiuyi.doctor.patients.v2.model.Patient;
import com.jiuyi.doctor.test.TestManager;

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

	// private @Autowired PatientServiceV2 patientService;

	private @Autowired TestManager testManager;

	@Test
	public void testLoadPatient() {
		Patient patient = testManager.loadPatient("164;truncate `test` #");
		assertNotNull(patient);
		System.err.println(patient.getName());
	}

}
