package com.example.web.dao;

import com.example.web.pojo.User;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    //模拟数据库中的数据
    private  static Map<Integer, User> Users = null;
    static {
        Users = new HashMap<Integer, User>();
        Users.put(1,new User(1,"1","1"));
        Users.put(2,new User(2,"2","2"));
        Users.put(3,new User(3,"3","3"));
        Users.put(4,new User(4,"4","4"));
    }
}
