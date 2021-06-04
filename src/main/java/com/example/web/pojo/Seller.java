package com.example.web.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    private Integer sellerId;
    private String name;
    private String password;
    private String brandName;
}
