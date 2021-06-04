package com.example.web.controller;

import com.example.web.pojo.OrderListPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderList {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/orderList")
    public String orderList_do(Model model, HttpSession session){
        if (session.getAttribute("loginName") != null) {
            int userId = Integer.parseInt(session.getAttribute("loginUserId").toString());
            List<OrderListPhone> orderListPhones;
            String sqlGet = "SELECT phone.*,orderlist.number,orderId,date FROM phone JOIN orderlist ON phone.phoneId = orderlist.phoneId " +
                    "WHERE phone.phoneId IN (SELECT phoneId FROM orderlist WHERE userId=?)";
            orderListPhones = jdbcTemplate.query(sqlGet, new Object[]{userId}, new BeanPropertyRowMapper<>(OrderListPhone.class));
            model.addAttribute("orderListPhones",orderListPhones);
            return "orderList";
        }
        return "redirect:/login";
    }

    @GetMapping("/deleteFromOrderList")
    public String deleteFromOrderList(@RequestParam(name = "orderId") Integer orderId,HttpSession session){
        if (session.getAttribute("loginName") != null) {
            String sqlDelete = "DELETE FROM orderlist WHERE orderId=?";
            jdbcTemplate.update(sqlDelete,orderId);
            return "forward:/orderList";
        }
        return "redirect:/login";
    }
}
