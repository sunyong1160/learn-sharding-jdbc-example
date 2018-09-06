package learn.sharding.jdbc.example.service.impl;

import learn.sharding.jdbc.example.mapper.UserMapper;
import learn.sharding.jdbc.example.model.dto.UserFacade;
import learn.sharding.jdbc.example.service.UserService;
import learn.sharding.jdbc.example.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunyong on 2018-09-05.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void save(User user) {
        userMapper.save(user);
    }

    @Override
    public void add(User user) {
        userMapper.save(user);
    }

    @Override
    public List<User> queryUser(UserFacade userFacade) {
        return userMapper.queryUser(userFacade);
    }
}
