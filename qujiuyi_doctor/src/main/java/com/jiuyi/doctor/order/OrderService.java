/**
 * 
 */
package com.jiuyi.doctor.order;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author xutaoyang
 *
 */
@Service
public class OrderService {

	private @Autowired OrderManager manager;

	public ThirdPayOrder loadOrderByTypeAndServiceId(OrderType orderType, String serviceId) {
		return manager.loadOrderByTypeAndServiceId(orderType, serviceId);
	}

}
