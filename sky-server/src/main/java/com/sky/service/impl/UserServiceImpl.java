package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserPageQueryDTO;
import com.sky.dto.UserRegistrationDTO;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import generator.domain.User;
import com.sky.service.UserService;
import com.sky.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 86133
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-10-26 01:42:32
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private UserMapper userMapper;
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public Long register(UserRegistrationDTO registrationDTO) {
        // 验证密码
        if (!registrationDTO.getPassword().equals(registrationDTO.getCheckPassword())) {
            throw new RuntimeException("两次密码不一致");
        }
        
        // 验证用户名
        if (registrationDTO.getUsername() == null || registrationDTO.getUsername().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        
        // 检查用户名是否已存在
        if (userMapper.countByUsername(registrationDTO.getUsername()) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        BeanUtils.copyProperties(registrationDTO, user);
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex(registrationDTO.getPassword().getBytes()));
        user.setCreatetime(new java.util.Date());
        user.setUpdatetime(new java.util.Date());
        user.setAdmin(false);
        user.setIsdelete(0);
        Integer id1 = userMapper.insertUser(user);
        
        return Long.valueOf(id1);
    }


    public User login(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        if(username == null || username.length() < 4 || password == null || password.length() < 8){
            log.info("用户名或密码错误0");
            throw new RuntimeException("用户名或密码错误0");
        }
        // 将 MD5 密码转换为大写
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

        User user = userMapper.login(username);
        String pwd = user.getPassword().toLowerCase();
        if(user == null){
            log.info("用户名或密码错误1");
            throw new RuntimeException("用户名或密码错误1");
        }
        else if(!pwd.equals(md5Password)){
            log.info("用户名或密码错误2");
            throw new RuntimeException("用户名或密码错误2");
        }
        request.setAttribute("USER_LOGIN",1);

        return user;
    }


    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        Page<User> page = userMapper.pageQuery(userPageQueryDTO);
        Long total = page.getTotal();
        List<User> res = page.getResult();
        for(User user: res){
            log.info(user.getUsername(), user.getPassword());
        }
        return new PageResult(total, res);
    }


    public void deleteById(Long id) {
        log.info("删除用户，id为：{}", id);
        userMapper.deleteById(id);

    }


    public void logout(HttpServletRequest request) {
        request.removeAttribute("USER_LOGIN");

    }


}






