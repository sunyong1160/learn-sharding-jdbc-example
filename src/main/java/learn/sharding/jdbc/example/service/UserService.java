package learn.sharding.jdbc.example.service;

import learn.sharding.jdbc.example.model.dto.UserFacade;
import learn.sharding.jdbc.example.model.vo.User;

import java.util.List;

/**
 * Created by sunyong on 2018-09-05.
 */
public interface UserService {

    void save(User user);

    void add(User user);

    List<User> queryUser(UserFacade userFacade);
}
