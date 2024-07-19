package com.richardvynz.stock_app.controller;


import com.richardvynz.stock_app.dto.StockResponseDTO;
import com.richardvynz.stock_app.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToController;

@WebFluxTest(StockController.class)
public class StockControllerTest {

    @MockBean
    private StockService stockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = bindToController(new StockController(stockService)).build();
    }

    @Test
    void testGetAllStocks() {
        StockResponseDTO stock1 = new StockResponseDTO(1L, "Apple Inc.", 150.0, LocalDateTime.now(), LocalDateTime.now());
        StockResponseDTO stock2 = new StockResponseDTO(2L, "Microsoft Corporation", 300.0, LocalDateTime.now(), LocalDateTime.now());

        when(stockService.getAllStocks()).thenReturn(Flux.just(stock1, stock2));

        webTestClient.get().uri("/api/stocks")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(StockResponseDTO.class)
                .hasSize(2)
                .contains(stock1, stock2);
    }

    @Test
    void testGetStockById() {
        StockResponseDTO stock = new StockResponseDTO(1L, "Apple Inc.", 150.0, LocalDateTime.now(), LocalDateTime.now());

        when(stockService.getStockById(1L)).thenReturn(Mono.just(stock));

        webTestClient.get().uri("/api/stocks/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(StockResponseDTO.class)
                .isEqualTo(stock);
    }


    @Test
    void testUpdateStockPrice() {
        StockResponseDTO updatedStock = new StockResponseDTO(1L, "Apple Inc.", 200.0, LocalDateTime.now(), LocalDateTime.now());

        when(stockService.updateStockPrice(1L, 200.0)).thenReturn(Mono.just(updatedStock));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/api/stocks/{id}")
                        .queryParam("newPrice", 200.0)
                        .build(1))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(StockResponseDTO.class)
                .isEqualTo(updatedStock);
    }



}
