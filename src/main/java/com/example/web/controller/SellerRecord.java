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
public class SellerRecord {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/sellerRecord")
    public String sellerRecord(Model model, HttpSession session, HttpServletRequest request) {
        if (session.getAttribute("sellerName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("sellerName").toString(),request,jdbcTemplate);

        String sellerName = session.getAttribute("sellerName").toString();
        String brandName = session.getAttribute("brandName").toString();
        model.addAttribute("sellerName", sellerName);
        model.addAttribute("brandName", brandName);
        //从数据库中取出自己负责的手机信息
        List<Phone> phones;
        String sqlPhone = "SELECT * FROM phone WHERE brandName = ?";
        phones = jdbcTemplate.query(sqlPhone, new Object[]{brandName}, new BeanPropertyRowMapper<>(Phone.class));
        model.addAttribute("phones", phones);
        //从数据库中取出浏览记录
        String sqlRecentView = "SELECT u.name as userName,p.name as phoneName,p.type,r.second,r.date FROM recentview r join phone p" +
                "    on r.phoneId = p.phoneId join user u on u.userId = r.userId WHERE p.brandName=?";
        List<Map<String,Object>> recentViews = jdbcTemplate.queryForList(sqlRecentView,brandName);
        model.addAttribute("recentViews", recentViews);
        //从数据库取出购买记录
        String sqlOrderList = "SELECT u.name as userName,p.name as phoneName,p.type,o.number,o.date FROM orderlist o join phone p" +
                "    on o.phoneId = p.phoneId join user u on u.userId = o.userId WHERE p.brandName=?";
        List<Map<String,Object>> orderLists = jdbcTemplate.queryForList(sqlOrderList,brandName);
        model.addAttribute("orderLists", orderLists);
        return "seller/record";
    }

    @ResponseBody
    @GetMapping("/sellerRecordData")
    public String sellerRecordData(HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("sellerName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("sellerName").toString(),request,jdbcTemplate);

        String brandName = session.getAttribute("brandName").toString();
        String sql = "SELECT phoneId,(SELECT SUM(number)FROM orderlist WHERE phone.phoneId = orderlist.phoneId)as number FROM phone WHERE brandName=?";
        List<Map<String,Object>> results = jdbcTemplate.queryForList(sql,brandName);
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
