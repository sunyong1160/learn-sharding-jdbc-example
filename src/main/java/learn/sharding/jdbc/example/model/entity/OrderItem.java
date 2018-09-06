package learn.sharding.jdbc.example.model.entity;

import lombok.Data;

@Data
public class OrderItem {

    private int itemId;

    private int userId;

    private int orderId;


}
