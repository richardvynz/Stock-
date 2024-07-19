package com.richardvynz.stock_app.service;
// StockService.java

import com.richardvynz.stock_app.dto.StockRequestDTO;
import com.richardvynz.stock_app.dto.StockResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StockService {
    Flux<StockResponseDTO> getAllStocks();
    Mono<StockResponseDTO> getStockById(Long id);
    Mono<StockResponseDTO> updateStockPrice(Long id, double newPrice);
    Mono<StockResponseDTO> createStock(StockRequestDTO stockRequestDTO);
}

