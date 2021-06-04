package com.example.web.controller;

import com.example.web.pojo.ShopCarPhone;
import com.example.web.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Controller
public class Settle {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MailService mailService;

    //结算用户购物栏中的商品
    @GetMapping("/settle")
    public String settle(Model model, HttpSession session) {
        if (session.getAttribute("loginName") != null) {
            int userId = Integer.parseInt(session.getAttribute("loginUserId").toString());
            //判断购物车中是否有购买数量小于1的商品，若有则删除并添加到销售异常中去
            String sqlWrong = "SELECT * FROM cart WHERE number <= 0";
            List<Map<String, Object>> wrong = jdbcTemplate.queryForList(sqlWrong);
            if (wrong.size() > 0) {
                for (Map<String, Object> item : wrong) {
                    String sqlWrongDelete = "DELETE FROM cart WHERE cartId = ?";
                    jdbcTemplate.update(sqlWrongDelete, item.get("cartId"));
                    String sqlWrongInsert = "INSERT INTO cartwrong(userId, phoneId, number) values (?,?,?)";
                    jdbcTemplate.update(sqlWrongInsert, item.get("userId"), item.get("phoneId"), item.get("number"));
                }
            }
            //联合查询,直接获取购物车里的手机信息,发送给前端展示
            List<ShopCarPhone> shopCarPhones;
            String sqlGet = "SELECT phone.*,cart.number FROM phone JOIN cart ON phone.phoneId = cart.phoneId " +
                    "WHERE phone.phoneId IN (SELECT phoneId FROM cart WHERE userId=?)";
            shopCarPhones = jdbcTemplate.query(sqlGet, new Object[]{userId}, new BeanPropertyRowMapper<>(ShopCarPhone.class));
            int totalPrice = 0;
            for (ShopCarPhone shopCarPhone : shopCarPhones) {
                totalPrice += shopCarPhone.getPrice() * shopCarPhone.getNumber();
            }
            String sqlAddress = "SELECT address FROM user WHERE userId=?";
            List<Map<String, Object>> Addresses = jdbcTemplate.queryForList(sqlAddress, userId);
            model.addAttribute("address", Addresses.get(0).get("address"));
            model.addAttribute("shopCarPhones", shopCarPhones);
            model.addAttribute("totalPrice", totalPrice);

            //把购物车的记录添加到历史订单表中去
            String sqlCart = "SELECT userId,phoneId,number FROM cart WHERE userId = ?";
            List<Map<String, Object>> carts = jdbcTemplate.queryForList(sqlCart, userId);
            StringBuilder values = new StringBuilder();
            if (carts.size() > 0) {     //判断购物车中是否有商品
                for (Map<String, Object> cart : carts) {
                    values.append("(");
                    values.append(cart.get("userId")).append(",");
                    values.append(cart.get("phoneId")).append(",");
                    values.append(cart.get("number")).append("),");
                }
                values.deleteCharAt(values.length() - 1); //最后一项不需要加","删去最后的逗号
                String sqlInsertOrderList = "INSERT INTO orderlist(userId, phoneId, number) VALUES " + values;
                //System.out.println(sqlInsertOrderList);
                jdbcTemplate.update(sqlInsertOrderList);
                //删除已购买的项目
                String sqlDelete = "DELETE FROM cart WHERE userId=?";
                jdbcTemplate.update(sqlDelete, userId);
            }

            //发送购买清单给用户
            StringBuilder mailText = new StringBuilder();
            for (ShopCarPhone shopCarPhone : shopCarPhones) {
                mailText.append("手机名称：").append(shopCarPhone.getName()).append("<br>");
                mailText.append("手机类型：").append(shopCarPhone.getType()).append("<br>");
                mailText.append("手机数量：").append(shopCarPhone.getNumber()).append("<br>");
                mailText.append("手机总价：").append(shopCarPhone.getPrice() * shopCarPhone.getNumber()).append("<br>");
                mailText.append("<br><br>");
            }
            mailService.sendMail(session.getAttribute("loginName").toString(), mailText.toString());
        } else
            return "redirect:/login";
        return "settle";
    }
}
