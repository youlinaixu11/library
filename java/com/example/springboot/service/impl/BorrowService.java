package com.example.springboot.service.impl;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.Borrow;
import com.example.springboot.entity.Retur;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.BookMapper;
import com.example.springboot.mapper.BorrowMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IBorrowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BorrowService implements IBorrowService {

    @Resource
    BorrowMapper borrowMapper;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    UserMapper userMapper;


    @Override
    public List<Borrow> list() {
        return borrowMapper.list();
    }

    @Override
    public PageInfo<Borrow> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
        List<Borrow> borrows = borrowMapper.listByCondition(baseRequest);
        for (Borrow borrow : borrows) {
            LocalDate updatetime = borrow.getUpdatetime();
            if (updatetime == null) {
                // 根据业务需求处理，比如设置默认值或者跳过这个borrow对象
                continue;
            }
            LocalDate now = LocalDate.now();
            if (now.plusDays(1).isEqual(updatetime)) {   //当前日期过一天后到期
                borrow.setNote("即将到期");
            } else if (now.isEqual(updatetime)) { //当前日期为还书日期
                borrow.setNote("已到期");
            } else if (now.isAfter(updatetime)) {  //已过还书日期，但未归还
                borrow.setNote("逾期未还");
            } else {                                //距离还书日期多余两天
                borrow.setNote("正常借阅");
            }
        }
        return new PageInfo<>(borrows);
    }

    @Override
    public void save(Borrow obj) {
        Book book = bookMapper.getByname(obj.getBookName());
        if (Objects.isNull(book)) {
            throw new ServiceException("所借图书不存在");
        }
        if (book.getNums() < 1) {
            throw new ServiceException("图书数量不足");
        }
//        更新图书数量
        book.setNums(book.getNums() - 1);
        bookMapper.updateById(book);
//        当前日期加days
        obj.setUpdatetime(LocalDate.now().plusDays(obj.getDays()));
//        新增借阅记录
        borrowMapper.save(obj);
    }

    @Override
    public PageInfo<Retur> pageRetur(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
        return new PageInfo<>(borrowMapper.listReturByCondition(baseRequest));
    }


    @Transactional
    @Override
    public void saveRetur(Retur obj) {
        obj.setStatus("已归还");
        borrowMapper.updateStatus("已归还", obj.getId());
        obj.setReturnDate(LocalDate.now());
        borrowMapper.saveRetur(obj);
        bookMapper.updateByBookName(obj.getBookName());
    }

    @Override
    public Borrow getById(Integer id) {
        return borrowMapper.getById(id);
    }

    @Override
    public void update(Borrow obj) {
        obj.setUpdatetime(LocalDate.now());
        borrowMapper.updateById(obj);
    }

    @Override
    public void deleteById(Integer id) {
        borrowMapper.deleteById(id);
    }

    @Override
    public void deleteReturById(Integer id) {
        borrowMapper.deleteReturById(id);
    }


}
