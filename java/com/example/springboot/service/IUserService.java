package com.example.springboot.service;



import com.example.springboot.controller.request.BaseRequest;
import com.github.pagehelper.PageInfo;
import com.example.springboot.entity.User;

import java.util.List;

public interface IUserService {

    List<User> list();

    PageInfo<User> page(BaseRequest baseRequest);

    void save(User user);

    User getById(Integer id);


//    void updateById(User user);

    void update(User user);

    void deleteById(Integer id);
}
