package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Borrow {

        /**
         * id
         */
        private Integer id;

        /**
         * 书名
         */
        private String bookName;

        /**
         * 用户卡号
         */
        private String userId;

        /**
         * 用户姓名
         */
        private String userName;

        /**
         * 用户联系方式
         */
        private String userPhone;

        /**
         * 创建时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private LocalDate createtime;

        /**
         * 修改时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private LocalDate updatetime;

        private String status;
        private String note;//提醒状态
        private Integer days;

        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private LocalDate returnDate;

}
