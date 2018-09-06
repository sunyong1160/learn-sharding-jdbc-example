package learn.sharding.jdbc.example.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by sunyong on 2018-09-05.
 */
@Data
public class User implements Serializable {

    private Long id;

    private String city;

    private String name;

}
