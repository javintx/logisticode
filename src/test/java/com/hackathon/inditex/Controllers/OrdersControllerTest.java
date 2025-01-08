package com.hackathon.inditex.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Services.OrdersService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrdersController.class)
public class OrdersControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrdersService ordersService;

  @Test
  public void testCreateOrder() throws Exception {
    Order order = new Order();
    order.setId(1L);
    order.setCustomerId(1L);
    order.setSize("M");
    order.setStatus("PENDING");

    Mockito.when(ordersService.create(Mockito.any(Order.class))).thenReturn(order);

    mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"customerId\":1,\"size\":\"M\"}"))
        .andExpect(status().isCreated())
        .andExpect(content().json(
            "{\"orderId\":1,\"customerId\":1,\"size\":\"M\",\"assignedLogisticsCenter\":null,\"coordinates\":null,\"status\":\"PENDING\",\"message\":\"Order created successfully in PENDING status.\"}"));
  }

  @Test
  public void testGetAllOrders() throws Exception {
    Order order = new Order();
    order.setId(1L);
    order.setCustomerId(1L);
    order.setSize("M");
    order.setStatus("PENDING");

    Mockito.when(ordersService.getAllOrders()).thenReturn(Collections.singletonList(order));

    mockMvc.perform(get("/api/orders"))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "[{\"id\":1,\"customerId\":1,\"size\":\"M\",\"assignedCenter\":null,\"coordinates\":null,\"status\":\"PENDING\"}]"));
  }
}