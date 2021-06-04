package com.example.web.controller;

import com.example.web.MyUtil.Record;
import com.example.web.pojo.Phone;
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
public class SellerManage {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/sellerManage")
    public String sellerManage(Model model, HttpSession session, HttpServletRequest request) {
        if (session.getAttribute("sellerName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("sellerName").toString(),request,jdbcTemplate);
        //从数据库中取出要展示的数据
        String brandName = session.getAttribute("brandName").toString();
        String sellerName = session.getAttribute("sellerName").toString();
        List<Phone> phones;
        String sqlGet = "SELECT * FROM phone WHERE brandName = ?";
        phones = jdbcTemplate.query(sqlGet, new Object[]{brandName}, new BeanPropertyRowMapper<>(Phone.class));
        model.addAttribute("phones", phones);
        model.addAttribute("sellerName", sellerName);
        model.addAttribute("brandName", brandName);
        return "seller/manage";
    }

    @GetMapping("/sellerManageAdd")
    public String sellerManageAdd(@RequestParam(value = "name") String phoneName,
                                  @RequestParam(value = "price") Integer price,
                                  @RequestParam(value = "type") String type,
                                  @RequestParam(value = "stock") Integer stock,
                                  HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("sellerName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("sellerName").toString(),request,jdbcTemplate);

        String sql = "INSERT INTO phone(name, brandName, type, stock, price) VALUES (?,?,?,?,?)";
        String brandName = session.getAttribute("brandName").toString();
        jdbcTemplate.update(sql,phoneName,brandName,type,stock,price);
        return "redirect:/sellerManage";
    }
    @GetMapping("/sellerManageDelete")
    public String sellerManageDelete(@RequestParam(value = "phoneId") String phoneId,
                                  HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("sellerName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("sellerName").toString(),request,jdbcTemplate);

        String sql = "DELETE FROM phone WHERE phoneId = ?";
        jdbcTemplate.update(sql,phoneId);
        return "redirect:/sellerManage";
    }
    @GetMapping("/sellerManageUpdate")
    public String sellerManageUpdate(@RequestParam(value = "phoneId") String phoneId,
                                     @RequestParam(value = "name") String phoneName,
                                     @RequestParam(value = "price") Integer price,
                                     @RequestParam(value = "type") String type,
                                     @RequestParam(value = "stock") Integer stock,
                                     HttpSession session,HttpServletRequest request) {
        if (session.getAttribute("sellerName") == null) return "redirect:/login";
        Record.logRecord(session.getAttribute("sellerName").toString(),request,jdbcTemplate);

        String sql = "UPDATE phone SET name=?,price=?,type=?,stock=? WHERE phoneId=?";
        jdbcTemplate.update(sql,phoneName,price,type,stock,phoneId);
        return "redirect:/sellerManage";
    }
}
