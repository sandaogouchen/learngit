package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserPageQueryDTO;
import com.sky.dto.UserRegistrationDTO;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.result.UserPageQuery;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import com.sky.vo.UserLoginVO;
import generator.domain.User;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/register")
    public Result<Long> register(@RequestBody UserRegistrationDTO registrationDTO) {
        log.info("用户注册：{}", registrationDTO.getUsername());
        Long userId = userService.register(registrationDTO);
        return Result.success(userId);
    }

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request){

        User user = userService.login(userLoginDTO,request);
        BeanUtils.copyProperties(userLoginDTO, user);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO =  UserLoginVO.builder()
                .id(Long.valueOf(user.getId()))
                .username(user.getUsername())
                .avatarurl(user.getAvatarurl())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    @GetMapping("/search")
    public Result<PageResult> pageQuery(UserPageQueryDTO employeePageQueryDTO) {
        PageResult pageResult = userService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/delete")
    public Result deteleById(@RequestBody Long id){
        userService.deleteById(id);
        return Result.success();
    }
    @GetMapping("/current")
    public Result<User> getCurrentUser(HttpServletRequest request){
        Object obj = request.getAttribute("user");
        User currentUser = (User) obj;
        if(currentUser == null){
            throw new RuntimeException("用户未登录");
        }

        Long id = Long.valueOf(currentUser.getId());
        User Res = userService.getById(id);
        return Result.success(Res);
    }

    @PostMapping("/logout")
    public Result logout(@RequestBody HttpServletRequest request){
        if(request == null){
            return Result.error("用户未登录");
        }


        userService.logout(request);
        return Result.success();

    }

    private boolean isAdmin(HttpServletRequest request){
        Object obj = request.getAttribute("USER_LOGIN");
        User user = (User) obj;
        return user != null && user.isAdmin();
    }



}
