package com.example.web;

import com.example.web.MyUtil.MD5;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Map;

@SpringBootTest
class WebApplicationTests {

    @Autowired
    JavaMailSenderImpl mailSender;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void test() {
        String sqlCart = "SELECT userId,phoneId,number FROM cart WHERE userId = ?";
        List<Map<String, Object>> carts = jdbcTemplate.queryForList(sqlCart, 1);
        StringBuilder values = new StringBuilder();
        if (carts.size() > 0) {     //判断购物车中是否有商品
            for (Map<String, Object> cart : carts) {
                System.out.println(cart);
                values.append("(");
                values.append(cart.get("userId")).append(",");
                values.append(cart.get("phoneId")).append(",");
                values.append(cart.get("number")).append("),");
            }
        }
        System.out.println(values);
    }

}
