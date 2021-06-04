package com.example.web.controller;

import com.example.web.MyUtil.MD5;
import com.example.web.MyUtil.Record;
import com.example.web.pojo.Phone;
import com.example.web.pojo.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class RootManage {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/rootManage")
    public String rootManage(HttpSession session, Model model, HttpServletRequest request) {
        if (session.getAttribute("rootName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("rootName").toString(),request,jdbcTemplate);

        //从数据库中取出卖家数据
        String rootName = session.getAttribute("rootName").toString();
        List<Seller> sellers;
        String sqlGet = "SELECT * FROM seller";
        sellers = jdbcTemplate.query(sqlGet, new BeanPropertyRowMapper<>(Seller.class));
        model.addAttribute("sellers", sellers);
        model.addAttribute("rootName", rootName);
        //从数据库中取出要展示的手机数据
        List<Phone> phones;
        String sqlPhone = "SELECT * FROM phone";
        phones = jdbcTemplate.query(sqlPhone, new Object[]{}, new BeanPropertyRowMapper<>(Phone.class));
        model.addAttribute("phones", phones);
        return "/root/manage";
    }

    @GetMapping("/rootManageAdd")
    public String rootManageAdd(@RequestParam(value = "name") String sellerName,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "brandName") String brandName,
                                HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("rootName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("rootName").toString(),request,jdbcTemplate);

        password = MD5.encrypt(password);
        String sql = "INSERT INTO seller(name, password, brandName) VALUES (?,?,?)";
        jdbcTemplate.update(sql, sellerName, password, brandName);
        return "redirect:/rootManage";
    }

    @GetMapping("/rootManageDelete")
    public String rootManageDelete(@RequestParam(value = "sellerId") String sellerId,
                                   HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("rootName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("rootName").toString(),request,jdbcTemplate);

        String sql = "DELETE FROM seller WHERE sellerId = ?";
        jdbcTemplate.update(sql, sellerId);
        return "redirect:/rootManage";
    }

    @GetMapping("/rootManageUpdate")
    public String rootManageUpdate(@RequestParam(value = "sellerId") String sellerId,
                                   @RequestParam(value = "name") String sellerName,
                                   @RequestParam(value = "brandName") String brandName,
                                   @RequestParam(value = "password") String password,
                                   HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("rootName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("rootName").toString(),request,jdbcTemplate);

        String sql = "UPDATE seller SET name=?,brandName=?,password=? WHERE sellerId=?";
        jdbcTemplate.update(sql, sellerName, brandName, password, sellerId);
        return "redirect:/rootManage";
    }

    @GetMapping("/rootPhoneManageAdd")
    public String rootPhoneManageAdd(@RequestParam(value = "name") String phoneName,
                                     @RequestParam(value = "price") Integer price,
                                     @RequestParam(value = "type") String type,
                                     @RequestParam(value = "brandName") String brandName,
                                     @RequestParam(value = "stock") Integer stock,
                                     HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("rootName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("rootName").toString(),request,jdbcTemplate);

        String sql = "INSERT INTO phone(name, brandName, type, stock, price) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql, phoneName, brandName, type, stock, price);
        return "redirect:/rootManage";
    }

    @GetMapping("/rootPhoneManageDelete")
    public String rootPhoneManageDelete(@RequestParam(value = "phoneId") String phoneId,
                                        HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("rootName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("rootName").toString(),request,jdbcTemplate);

        String sql = "DELETE FROM phone WHERE phoneId = ?";
        jdbcTemplate.update(sql, phoneId);
        return "redirect:/rootManage";
    }

    @GetMapping("/rootPhoneManageUpdate")
    public String rootPhoneManageUpdate(@RequestParam(value = "phoneId") String phoneId,
                                        @RequestParam(value = "name") String phoneName,
                                        @RequestParam(value = "price") Integer price,
                                        @RequestParam(value = "type") String type,
                                        @RequestParam(value = "brandName") String brandName,
                                        @RequestParam(value = "stock") Integer stock,
                                        HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("rootName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("rootName").toString(),request,jdbcTemplate);

        String sql = "UPDATE phone SET name=?,price=?,type=?,brandName=?,stock=? WHERE phoneId=?";
        jdbcTemplate.update(sql, phoneName, price, type, brandName, stock, phoneId);
        return "redirect:/rootManage";
    }
}
