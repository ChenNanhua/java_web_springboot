package com.example.web.controller;

import com.example.web.MyUtil.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class RootBigData {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @GetMapping("/rootBigData")
    public String rootBigData(Model model, HttpSession session, HttpServletRequest request){
        if (session.getAttribute("rootName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("rootName").toString(),request,jdbcTemplate);

        //大数据 登录登出记录
        String sqlLoginRecord = "SELECT * FROM loginrecord ORDER BY date DESC ";
        List<Map<String,Object>> loginRecords = jdbcTemplate.queryForList(sqlLoginRecord);
        model.addAttribute("loginRecords",loginRecords);

        //大数据 操作记录
        String sqlLogRecord = "SELECT * FROM logrecord ORDER BY logId DESC ";
        List<Map<String,Object>> logRecords = jdbcTemplate.queryForList(sqlLogRecord);
        model.addAttribute("logRecords",logRecords);

        //大数据 获取用户画像，根据他们最常买哪种品牌
        String sql = "SELECT name,brandName,MAX(sum) as max FROM\n" +
                "(SELECT user.name, p.brandName,SUM(o.number) as sum FROM user join orderlist o join phone p on user.userId = o.userId\n" +
                "AND o.phoneId = p.phoneId GROUP BY brandName,user.name order by sum DESC) as t\n" +
                "GROUP BY name";
        List<Map<String,Object>> userPortrait= jdbcTemplate.queryForList(sql);
        model.addAttribute("users",userPortrait);

        //大数据 销售预测，获取销量最高的3部手机
        String sql1 = "SELECT p.name,p.type,p.brandName FROM orderlist join phone p on p.phoneId = orderlist.phoneId\n" +
                "GROUP BY p.name,p.type ORDER BY SUM(number) DESC LIMIT 0,3";
        List<Map<String,Object>> saleForecast = jdbcTemplate.queryForList(sql1);
        model.addAttribute("sales",saleForecast);

        //大数据 销售异常的判别
        String sql2 = "SELECT u.name as username,p.name as phoneName,p.type,p.brandName,c.number,c.date FROM cartwrong c join user u on u.userId = c.userId\n" +
                " join phone p on p.phoneId = c.phoneId";
        List<Map<String,Object>> saleWrong = jdbcTemplate.queryForList(sql2);
        model.addAttribute("salesWrong",saleWrong);
        return "root/bigData";
    }
}
