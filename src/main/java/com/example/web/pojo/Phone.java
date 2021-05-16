package com.example.web.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phone {
    private Integer phoneId;
    private String name;
    private String brandName;
    private String type;
    private Integer stock;
    private Integer price;
}
