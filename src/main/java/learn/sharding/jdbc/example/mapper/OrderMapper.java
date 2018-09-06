package learn.sharding.jdbc.example.mapper;


import learn.sharding.jdbc.example.model.entity.Order;

import java.util.List;

public interface OrderMapper {
	
	List<Order> getOrderListByUserId(int userId);
	
	void createOrder(Order order);

	void createOrder2(Order order);

}
