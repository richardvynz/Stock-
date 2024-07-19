package com.richardvynz.stock_app.controller;

import com.richardvynz.stock_app.dto.StockRequestDTO;
import com.richardvynz.stock_app.dto.StockResponseDTO;
import com.richardvynz.stock_app.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public Flux<StockResponseDTO> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<StockResponseDTO>> getStockById(@PathVariable Long id) {
        return stockService.getStockById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<StockResponseDTO>> updateStockPrice(@PathVariable Long id, @RequestParam double newPrice) {
        return stockService.updateStockPrice(id, newPrice)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<StockResponseDTO>> createStock(@RequestBody StockRequestDTO stockRequestDTO) {
        return stockService.createStock(stockRequestDTO)
                .map(newStock -> ResponseEntity.status(HttpStatus.CREATED).body(newStock))
                .onErrorResume(e -> {
                    System.err.println(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}
