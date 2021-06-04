package com.example.web.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderListPhone extends ShopCarPhone{
    public OrderListPhone(ShopCarPhone shopCarPhone, Integer orderId, Timestamp date){
        super(shopCarPhone);
        this.orderId = orderId;
        this.date = date;
    }
    private Integer orderId;
    private Timestamp date;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "UTC")
    public Timestamp getDate() {
        return date;
    }
}
