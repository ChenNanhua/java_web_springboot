package com.example.web.MyUtil;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Data
public class Record {
    public static String getIP(HttpServletRequest request) {
        String ipAddress = "";
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();
        return ipAddress;
    }

    public static String getURL(HttpServletRequest request) {
        if (request.getQueryString() == null) {
            return request.getRequestURI();
        } else
            return request.getRequestURI() + "?" + request.getQueryString();
    }

    public static void loginRecord(String name, boolean type, HttpServletRequest request, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO loginRecord(name, type,IP) values (?,?,?)";
        jdbcTemplate.update(sql, name, type, getIP(request));
    }

    public static void logRecord(String name, HttpServletRequest request, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO logRecord(name, url, IP) values (?,?,?)";
        jdbcTemplate.update(sql, name, getURL(request), getIP(request));
    }

}
