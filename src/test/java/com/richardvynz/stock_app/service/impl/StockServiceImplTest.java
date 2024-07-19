package com.richardvynz.stock_app.service.impl;

import com.richardvynz.stock_app.dto.StockRequestDTO;
import com.richardvynz.stock_app.dto.StockResponseDTO;
import com.richardvynz.stock_app.entity.Stock;
import com.richardvynz.stock_app.exception.StockNotFoundException;
import com.richardvynz.stock_app.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

public class StockServiceImplTest {

    private StockService stockService;

    private Stock stock1;
    private Stock stock2;

    @BeforeEach
    void setUp() {
        stockService = new StockServiceImpl();

        stock1 = new Stock(1L, "Apple Inc.", 150.0, LocalDateTime.now(), LocalDateTime.now());
        stock2 = new Stock(2L, "Microsoft Corporation", 300.0, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testGetAllStocks() {
        Flux<StockResponseDTO> stocks = stockService.getAllStocks();

        StepVerifier.create(stocks)
                .expectNextMatches(stock -> stock.getName().equals("Apple Inc.") && stock.getCurrentPrice() == 150.0)
                .expectNextMatches(stock -> stock.getName().equals("Microsoft Corporation") && stock.getCurrentPrice() == 300.0)
                .verifyComplete();
    }

    @Test
    void testGetStockById() {
        Mono<StockResponseDTO> stock = stockService.getStockById(1L);

        StepVerifier.create(stock)
                .expectNextMatches(response -> response.getName().equals("Apple Inc.") && response.getCurrentPrice() == 150.0)
                .verifyComplete();
    }

    @Test
    void testGetStockById_NotFound() {
        Mono<StockResponseDTO> stock = stockService.getStockById(3L);

        StepVerifier.create(stock)
                .expectError(StockNotFoundException.class)
                .verify();
    }

    @Test
    void testUpdateStockPrice() {
        Mono<StockResponseDTO> updatedStock = stockService.updateStockPrice(1L, 200.0);

        StepVerifier.create(updatedStock)
                .expectNextMatches(stock -> stock.getCurrentPrice() == 200.0)
                .verifyComplete();
    }

    @Test
    void testUpdateStockPrice_NotFound() {
        Mono<StockResponseDTO> updatedStock = stockService.updateStockPrice(3L, 200.0);

        StepVerifier.create(updatedStock)
                .expectError(StockNotFoundException.class)
                .verify();
    }

    @Test
    void testCreateStock() {
        StockRequestDTO newStock = new StockRequestDTO("Google LLC", 2800.0);

        Mono<StockResponseDTO> createdStock = stockService.createStock(newStock);

        StepVerifier.create(createdStock)
                .expectNextMatches(stock -> stock.getName().equals("Google LLC") && stock.getCurrentPrice() == 2800.0)
                .verifyComplete();
    }
}
