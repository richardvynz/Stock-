package com.richardvynz.stock_app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private Long id;
    private String name;
    private Double currentPrice;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
}
