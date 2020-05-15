package com.gateway.dao;

import com.gateway.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    @Select("select * from user where username=#{username} and password=#{password}")
    User login(String username, String password);

    @Select("select * from user")
    List<User> getAllUser();


}
