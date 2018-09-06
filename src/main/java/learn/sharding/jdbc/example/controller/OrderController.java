package learn.sharding.jdbc.example.controller;

import learn.sharding.jdbc.example.model.entity.Order;
import learn.sharding.jdbc.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;


@SuppressWarnings("Duplicates")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(path = "/{userId}", method = {RequestMethod.GET})
    public List<Order> getOrderListByUserId(@PathVariable("userId") Integer userId) {
        return orderService.getOrderListByUserId(userId);
    }

    @RequestMapping(path = "/{userId}/{orderId}", method = {RequestMethod.POST})
    public String createOrderRest(@PathVariable("userId") Integer userId, @PathVariable("orderId") Long orderId,
                                  @PathVariable("status") String status) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setStatus(status);
        orderService.createOrder(order);
        return "success";
    }

    @RequestMapping("/createOrder")
    public void createOrder() {
        for (long i = 0; i < 100; i++) {
            Order order = new Order();
            order.setOrderId(i);
            int j = Integer.parseInt(String.valueOf(i));
            order.setUserId(j);
            order.setStatus("成功");
            orderService.createOrder(order);
        }
    }

    @RequestMapping("/createOrder2")
    public void createOrder2() {
        for (long i = 0; i < 100; i++) {
            Order order = new Order();
            order.setOrderId(i);
            int j = Integer.parseInt(String.valueOf(i));
            order.setUserId(j);
            if (i % 4 == 0) {
                order.setCity("上海");
            } else if (i % 4 == 1) {
                order.setCity("南京");
            } else if (i % 4 == 2) {
                order.setCity("合肥");
            } else if (i % 4 == 3) {
                order.setCity("天津");
            }
            order.setStatus("成功");
            orderService.createOrder2(order);
        }
    }
}
