package com.example.web.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopCarPhone extends Phone {
    public ShopCarPhone(Phone phone, Integer number) {
        super(phone);
        this.number = number;
    }

    public ShopCarPhone(ShopCarPhone shopCarPhone) {
        super(shopCarPhone.phoneId, shopCarPhone.name, shopCarPhone.brandName, shopCarPhone.type, shopCarPhone.stock, shopCarPhone.price);
        this.number = shopCarPhone.number;
    }

    private Integer number;
}
