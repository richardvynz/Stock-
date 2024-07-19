package com.richardvynz.stock_app.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StockResponseDTO {
    private Long id;
    private String name;
    private double currentPrice;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;

}
