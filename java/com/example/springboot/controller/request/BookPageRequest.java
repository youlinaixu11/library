package com.example.springboot.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookPageRequest extends BaseRequest {
    private String name;
    private String bookNo;
}
