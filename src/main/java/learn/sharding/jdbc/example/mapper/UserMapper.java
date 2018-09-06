package learn.sharding.jdbc.example.mapper;

import learn.sharding.jdbc.example.model.dto.UserFacade;
import learn.sharding.jdbc.example.model.entity.User;

import java.util.List;

/**
 * Created by sunyong on 2018-09-05.
 */
public interface UserMapper {

    void save(User user);

    List<User> queryUser(UserFacade userFacade);

}
