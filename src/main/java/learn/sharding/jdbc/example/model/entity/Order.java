package learn.sharding.jdbc.example.model.entity;

import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Order implements Serializable {

    private int userId;

    private Long orderId;

    private String status;

    private String city;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
