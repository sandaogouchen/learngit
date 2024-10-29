package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.UserPageQueryDTO;
import generator.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
* @author 86133
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-10-26 01:42:32
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int countByUsername(String username);

    @Insert("INSERT INTO user (username, password, create_time, update_time, is_delete) " +
            "VALUES (#{username}, #{password}, #{createtime}, #{updatetime}, #{isvalid})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User login(String username);


    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);
}




