package com.example.web.controller;

import com.example.web.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JDBCTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/JDBCtest")
    public String JDBCTest(){
        String sql = selectTable("user");
        return jdbcTemplate.queryForList(sql).toString();
    }
    //选择要查询的数据库
    public String selectTable(String table) {
        return "select * from " + table + " ";
    }
    //select全部
    public List<Map<String, Object>> selectAll(String table) {
        String sql = selectTable(table);
        System.out.println(sql);
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> select(String table, int id) {
        String sql = selectTable(table) + "where ?Id = ?";
        return jdbcTemplate.queryForList(sql,table,id);
    }

    //从数据库以Map<String,Object>的形式查询数据
    public List<Map<String,Object>> selectMap(){
        String sql = "select * from user where userId = ?";
        List<Map<String,Object>> maps = jdbcTemplate.queryForList(sql,1);
        System.out.println(maps.get(0).get("name"));
        System.out.println(maps.get(0));
        return maps;
    }

    //从数据库以类的形式查询数据
    public List<User> selectClass(){
        String sql = "select * from user where userId > ?";
        List<User> lists = jdbcTemplate.query(sql,new Object[]{0},new BeanPropertyRowMapper<>(User.class));
        return lists;
    }
}
