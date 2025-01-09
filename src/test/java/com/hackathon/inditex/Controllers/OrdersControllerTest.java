package com.hackathon.inditex.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Services.OrdersService;
import com.hackathon.inditex.Services.OrdersService.NotProcessedOrder;
import com.hackathon.inditex.Services.OrdersService.ProcessedOrder;
import java.util.Collections;
import java.util.List;
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
    var order = new Order();
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
    var order = new Order();
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

  @Test
  public void testOrderAssignations() throws Exception {
    var processedOrders = List.of(
        new ProcessedOrder(10.0, 1L, "Center A"),
        new NotProcessedOrder(2.0, 2L, null, "Not processed", "PENDING")
    );

    Mockito.when(ordersService.orderAssignations()).thenReturn(processedOrders);

    mockMvc.perform(post("/api/orders/order-assignations")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\"processed-orders\":[{\"distance\":10.0,\"orderId\":1,\"assignedLogisticsCenter\":\"Center A\",\"status\":\"ASSIGNED\"},{\"distance\":2,\"orderId\":2,\"assignedLogisticsCenter\":null,\"message\":\"Not processed\",\"status\":\"PENDING\"}]}"));
  }
}