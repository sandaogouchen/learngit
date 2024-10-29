package com.sky.dto;

import com.sky.result.UserPageQuery;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserPageQueryDTO implements Serializable {
    private  String username;
    private int  page;
    private int pageSize;

}
