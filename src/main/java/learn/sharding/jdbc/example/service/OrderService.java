package learn.sharding.jdbc.example.service;

import learn.sharding.jdbc.example.mapper.OrderMapper;
import learn.sharding.jdbc.example.model.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	public List<Order> getOrderListByUserId(Integer userId) {
		return orderMapper.getOrderListByUserId(userId);
	}
	
	public void createOrder(Order order) {
		orderMapper.createOrder(order);
	}


	public void createOrder2(Order order) {
		orderMapper.createOrder2(order);
	}

}
