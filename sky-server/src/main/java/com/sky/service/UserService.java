package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserPageQueryDTO;
import com.sky.dto.UserRegistrationDTO;
import com.sky.result.PageResult;
import com.sky.vo.UserLoginVO;
import generator.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService extends IService<User> {
    Long register(UserRegistrationDTO registrationDTO);

    User login(UserLoginDTO userLoginDTO, HttpServletRequest request);


    PageResult pageQuery(UserPageQueryDTO employeePageQueryDTO);

    void deleteById(Long id);

    void logout(HttpServletRequest request);
}
