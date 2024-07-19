package com.richardvynz.stock_app.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockRequestDTO {
    private String name;
    private double currentPrice;

}
