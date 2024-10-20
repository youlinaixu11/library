package com.example.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.springboot.controller.dto.loginDTO;
import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.controller.request.loginRequest;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.AdminMapper;
import com.example.springboot.service.IAdminService;
import com.example.springboot.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.example.springboot.entity.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AdminService implements IAdminService {

    @Autowired
    AdminMapper adminMapper;
    private Object admin;

    private String securePass(String password) {
        return SecureUtil.md5(password + PASS_SALT);
    }

    private static final String DEFAULT_PASS = "123456";
    private static final String PASS_SALT = "TUSHUGUANLI";

    @Override
    public List<Admin> list() {

        return adminMapper.list();
    }

    @Override
    public PageInfo<Admin> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());
        List<Admin> users = adminMapper.listByCondition(baseRequest);
        return new PageInfo<>(users);
    }

    @Override
    public void save(Admin obj) {
//        默认密码 123456
        if (StrUtil.isBlank(obj.getPassword())){
            obj.setPassword(DEFAULT_PASS);
        }
        obj.setPassword( securePass(obj.getPassword()) );
            adminMapper.save(obj);
    }

    @Override
    public Admin getById(Integer id) {
        return adminMapper.getById(id);
    }


    @Override
    public void update(Admin user) {
        user.setUpdatetime(new Date());
        // 执行数据库更新操作
        adminMapper.updateById(user);
    }

    @Override
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    @Override
    public loginDTO login(loginRequest request) {

        request.setPassword(securePass(request.getPassword()));
        Admin admin = adminMapper.getByUsernameAndPassword(request.getUsername(),request.getPassword());
        if (admin == null) {
            throw new ServiceException("用户名或密码错误");
        }
        if (!admin.isStatus()) {
            throw new ServiceException("当前帐号处于禁用状态，请重试");
        }
        loginDTO loginDTO = new loginDTO();
        BeanUtils.copyProperties(admin, loginDTO);

        String token = TokenUtils.genToken(String.valueOf(admin.getId()), admin.getPassword());
        loginDTO.setToken(token);
        return loginDTO;
    }

    @Override
    public void changePass(PasswordRequest request) {
        request.setNewPass(securePass(request.getNewPass()));
        int count = adminMapper.updatePassword(request);
        if (count <= 0) {
            throw new ServiceException("修改密码失败");
        }
    }


}
