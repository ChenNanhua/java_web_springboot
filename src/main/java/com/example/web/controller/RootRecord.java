package com.example.web.controller;

import com.example.web.MyUtil.Record;
import com.example.web.pojo.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class RootRecord {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @GetMapping("/rootRecord")
    public String rootRecord(Model model, HttpSession session, HttpServletRequest request) {
        if (session.getAttribute("rootName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("rootName").toString(),request,jdbcTemplate);

        String rootName = session.getAttribute("rootName").toString();
        model.addAttribute("rootName", rootName);
        //从数据库中取出所有的手机销售情况
        List<Phone> phones;
        String sqlPhone = "SELECT * FROM phone";
        phones = jdbcTemplate.query(sqlPhone, new Object[]{}, new BeanPropertyRowMapper<>(Phone.class));
        model.addAttribute("phones", phones);
        //从数据库中取出浏览记录
        String sqlRecentView = "SELECT u.name as userName,p.name as phoneName,p.type,p.brandName,r.second,r.date FROM recentview r join phone p" +
                "    on r.phoneId = p.phoneId join user u on u.userId = r.userId";
        List<Map<String,Object>> recentViews = jdbcTemplate.queryForList(sqlRecentView);
        model.addAttribute("recentViews", recentViews);
        //从数据库取出购买记录
        String sqlOrderList = "SELECT u.name as userName,p.name as phoneName,p.type,p.brandName,o.number,o.date FROM orderlist o join phone p" +
                "    on o.phoneId = p.phoneId join user u on u.userId = o.userId";
        List<Map<String,Object>> orderLists = jdbcTemplate.queryForList(sqlOrderList);
        model.addAttribute("orderLists", orderLists);
        //从数据库取出单个商品的销售记录
        String sqlPhoneSell = "SELECT p.name as phoneName,p.type,SUM(o.number)as number,SUM(o.number * p.price) as price " +
                "FROM orderlist o join phone p on o.phoneId = p.phoneId GROUP BY phoneName,type ORDER BY phoneName";
        List<Map<String,Object>> phoneSells = jdbcTemplate.queryForList(sqlPhoneSell);
        model.addAttribute("phoneSells", phoneSells);
        return "root/record";
    }


    @ResponseBody
    @GetMapping("/rootRecordData")
    public String rootRecordData(HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("rootName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("rootName").toString(),request,jdbcTemplate);

        String sql = "SELECT phoneId,(SELECT SUM(number)FROM orderlist WHERE phone.phoneId = orderlist.phoneId)as number FROM phone";
        List<Map<String,Object>> results = jdbcTemplate.queryForList(sql);
        StringBuilder result1 = new StringBuilder();
        StringBuilder result2 = new StringBuilder();
        for (Map<String,Object> result:results){
            result1.append(result.get("phoneId")).append(",");
            result2.append(result.get("number")).append(",");
        }
        result1.deleteCharAt(result1.length()-1);   //删去最后一个","
        result2.deleteCharAt(result2.length()-1);

        return result1.append("#").append(result2).toString();
    }
}
