package com.example.web.controller;

import com.example.web.pojo.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class Index {
    @Autowired
    JdbcTemplate jdbcTemplate;


    //查询要展示的手机信息
    @RequestMapping({"/", "index.html", "/index"})
    public String index(@RequestParam(value = "brandName",required = false) String brandName,
                        Model model) {
        List<Phone> phones;
        if (brandName!=null && !brandName.equals("")) {
            String sql = "select * from phone where brandName = ?";
            phones = jdbcTemplate.query(sql, new Object[]{brandName}, new BeanPropertyRowMapper<>(Phone.class));
        } else {
            String sql = "select * from phone";
            phones = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Phone.class));
        }
        model.addAttribute("phones", phones);
        return "index";
    }
    @RequestMapping("info")
    public void info(@RequestParam(value = "phoneId") String phoneId){

    }
}
