package com.example.springboot.service;

import com.example.springboot.controller.dto.loginDTO;
import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.controller.request.loginRequest;
import com.github.pagehelper.PageInfo;
import com.example.springboot.entity.Admin;

import java.util.List;

public interface IAdminService {

    List<Admin> list();

    PageInfo<Admin> page(BaseRequest baseRequest);

    void save(Admin obj);

    Admin getById(Integer id);


    void update(Admin obj);

    void deleteById(Integer id);

    loginDTO login(loginRequest request);

    void changePass(PasswordRequest request);
}
