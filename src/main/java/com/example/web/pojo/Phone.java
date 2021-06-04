package com.example.web.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phone {
    public Phone(Phone phone){
        this.brandName = phone.getBrandName();
        this.name = phone.getName();
        this.phoneId = phone.getPhoneId();
        this.price = phone.getPrice();
        this.stock = phone.getStock();
        this.type = phone.getType();
    }
    protected Integer phoneId;
    protected String name;
    protected String brandName;
    protected String type;
    protected Integer stock;
    protected Integer price;
}
