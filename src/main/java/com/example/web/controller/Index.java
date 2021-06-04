package com.example.web.controller;

import ch.qos.logback.classic.sift.JNDIBasedContextDiscriminator;
import com.example.web.pojo.Phone;
import com.example.web.pojo.ShopCarPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class Index {
    @Autowired
    JdbcTemplate jdbcTemplate;


    //查询要展示的手机信息
    @GetMapping({"/", "index.html", "/index"})
    public String index(@RequestParam(name = "brandName", required = false) String brandName,
                        @RequestParam(name = "number", required = false) Integer number,
                        @RequestParam(name = "delete", required = false) Boolean delete,
                        @RequestParam(name = "phoneId", required = false) String phoneId,
                        Model model, HttpSession session) {
        //品牌分类
        List<Map<String,Object>> brands;
        String sqlBrand = "SELECT brandName FROM phone GROUP BY brandName";
        brands = jdbcTemplate.queryForList(sqlBrand);
        model.addAttribute("brands",brands);

        //手机显示页面的初始化
        List<Phone> phones, quickViewPhones = new LinkedList<>();
        //根据品牌显示首页信息
        if (brandName != null && !brandName.equals("")) {
            String sql = "SELECT * FROM phone WHERE brandName = ?";
            phones = jdbcTemplate.query(sql, new Object[]{brandName}, new BeanPropertyRowMapper<>(Phone.class));
        } else {
            String sqlAll = "SELECT * FROM phone";
            phones = jdbcTemplate.query(sqlAll, new BeanPropertyRowMapper<>(Phone.class));
        }
        model.addAttribute("phones", phones);

        List<Phone> recentViews = new LinkedList<>();
        List<Phone> recommends = new LinkedList<>();
        if (session.getAttribute("loginName") != null) {
            //最近浏览栏的初始化,已登录用户才展示最近浏览栏,展示5条信息
            int userId = Integer.parseInt(session.getAttribute("loginUserId").toString());
            String sqlRecent = "SELECT * FROM phone WHERE phoneId IN (SELECT phoneId FROM recentView where userId = ?) LIMIT 0,5";
            recentViews = jdbcTemplate.query(sqlRecent, new Object[]{userId}, new BeanPropertyRowMapper<>(Phone.class));
            model.addAttribute("recentViews", recentViews);

            //大数据 推荐商品栏的初始化
            String sqlRecommend = "SELECT phone.* FROM phone join recentview r on phone.phoneId = r.phoneId WHERE userId = ?\n" +
                    "GROUP BY r.phoneId ORDER BY SUM(r.second) DESC LIMIT 0,5";
            recommends = jdbcTemplate.query(sqlRecommend,new Object[]{userId},new BeanPropertyRowMapper<>(Phone.class));
            model.addAttribute("recommends",recommends);
        }
        //手机弹窗页面的数据集合
        quickViewPhones.addAll(phones);
        quickViewPhones.addAll(recentViews);
        quickViewPhones.addAll(recommends);
        model.addAttribute("quickViewPhones", quickViewPhones);

        //对购物车的操作
        if (session.getAttribute("loginName") != null) {
            int userId = Integer.parseInt(session.getAttribute("loginUserId").toString());
            //处理添加到购物车的请求
            if (number != null && phoneId != null) {
                //准备插入要购买的手机数据到cart表
                try {   //如果插入的购买信息重复，则改为更新信息
                    String sqlInsertShop = "INSERT INTO cart(userId, phoneId, number) values (?,?,?)";
                    jdbcTemplate.update(sqlInsertShop, userId, phoneId, number);
                } catch (Exception e) {
                    System.out.println("cart插入重复数据，正在改正");
                    String sqlInsertShop = "UPDATE cart SET number = number+? WHERE userId=? AND phoneId=?";
                    jdbcTemplate.update(sqlInsertShop, number, userId, phoneId);
                }
            }
            //删除购物车中的项目
            if (delete != null) {
                String sqlDeleteShop = "DELETE FROM cart WHERE userId=? AND phoneId=?";
                jdbcTemplate.update(sqlDeleteShop, userId, phoneId);
            }
            //购物车界面的展示
            //联合查询,直接获取购物车里的手机信息
            List<ShopCarPhone> shopCarPhones;
            String sqlGetShop = "SELECT phone.*,cart.number FROM phone JOIN cart ON phone.phoneId = cart.phoneId " +
                    "WHERE phone.phoneId IN (SELECT phoneId FROM cart WHERE userId=?)";
            shopCarPhones = jdbcTemplate.query(sqlGetShop, new Object[]{userId}, new BeanPropertyRowMapper<>(ShopCarPhone.class));
            int totalPrice = 0;
            for (ShopCarPhone shopCarPhone : shopCarPhones) {
                totalPrice += shopCarPhone.getPrice() * shopCarPhone.getNumber();
            }
            model.addAttribute("shopCarPhones", shopCarPhones);
            model.addAttribute("totalPrice", totalPrice);
        } else {
            if (number != null || phoneId != null) {    //未登录用户添加商品到购物车进行处理
                model.addAttribute("msg", "登录后才能添加到购物车");
                return "index";
            }
        }
        return "index";
    }
}

