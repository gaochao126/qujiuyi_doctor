/**
 * 
 */
package com.jiuyi.doctor.test.call;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.doctor.call.CallManager;
import com.jiuyi.frame.front.ServerResult;

import junit.framework.TestCase;

/**
 * 
 * @author xutaoyang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/servlet-context.xml", "/root-context.xml" })
@Transactional
public class CallTest extends TestCase {

	// private @Autowired PatientServiceV2 patientService;

	private @Autowired CallManager callManager;

	@Test
	public void testMakeCall() {
		ServerResult res = callManager.makeCall("18223506390", "13635401747");
		System.out.println(res);
	}

}
