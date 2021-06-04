package com.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
public class ShopRecord {
    @Autowired
    JdbcTemplate jdbcTemplate;

    //记录用户查看了什么商品
    @GetMapping("/info")
    public void info(@RequestParam("phoneId") String phoneId,
                     @RequestParam("second") String second,
                     HttpSession session) {
        if (session.getAttribute("loginName") != null) {
            try {   //可能插入重复数据
                int userId = Integer.parseInt(session.getAttribute("loginUserId").toString());
                String sql = "INSERT INTO recentView (userId,phoneId,second) VALUES (?,?,?)";
                int count = jdbcTemplate.update(sql, userId, phoneId, second);
                //删除某个用户大于十条的浏览信息
                if (count == 1) {
                    String sqlSelect = "SELECT * FROM recentView WHERE userId = ?";
                    //判断用户浏览记录条数是否过多
                    List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlSelect, userId);
                    if (list.size() > 1000) {
                        String sqlDelete = "DELETE\n" +
                                "FROM recentView\n" +
                                "WHERE id in (\n" +
                                "    SELECT id\n" +
                                "    FROM (\n" +
                                "             SELECT id\n" +
                                "             FROM recentView\n" +
                                "             WHERE userId = ?\n" +
                                "             ORDER BY id DESC\n" +
                                "             LIMIT 1000, 2000\n" +
                                "         ) as ids)\n";
                        jdbcTemplate.update(sqlDelete, userId);  //删除多余1000条的个人浏览记录
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                System.out.println("recentView插入了重复数据");
            }
        }
    }
}
