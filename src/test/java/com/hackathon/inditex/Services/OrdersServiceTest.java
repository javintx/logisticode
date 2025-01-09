package com.hackathon.inditex.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Repositories.OrdersRepository;
import com.hackathon.inditex.Services.OrdersService.NotProcessedOrder;
import com.hackathon.inditex.Services.OrdersService.ProcessedOrder;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OrdersServiceTest {

  @Mock
  private OrdersRepository ordersRepository;
  @Mock
  private CentersService centersService;
  @InjectMocks
  private OrdersService ordersService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateOrder() {
    var order = new Order();
    order.setSize("m");

    when(ordersRepository.save(order)).thenReturn(order);

    var createdOrder = ordersService.create(order);

    assertNotNull(createdOrder);
    assertEquals("PENDING", createdOrder.getStatus());
    verify(ordersRepository, times(1)).save(order);
  }


  @Test
  void testGetAllOrders() {
    var order1 = new Order();
    var order2 = new Order();
    var orders = Arrays.asList(order1, order2);

    when(ordersRepository.findAll()).thenReturn(orders);

    var result = ordersService.getAllOrders();

    assertEquals(2, result.size());
    verify(ordersRepository, times(1)).findAll();
  }

  @Test
  void testOrderAssignations() {
    var order = new Order();
    order.setId(1L);
    order.setSize("m");
    order.setCoordinates(new Coordinates(40.416775, -3.703790));
    order.setStatus("PENDING");

    var center = new Center();
    center.setId(1L);
    center.setName("Center 1");
    center.setCoordinates(new Coordinates(40.416775, -3.703790));
    center.setCurrentLoad(5);
    center.setMaxCapacity(10);
    center.setCapacity("m");

    when(ordersRepository.findByStatusOrderById("PENDING")).thenReturn(List.of(order));
    when(centersService.retrieveAllLogisticsCenters()).thenReturn(List.of(center));

    var result = ordersService.orderAssignations();

    assertNotNull(result);
    assertInstanceOf(ProcessedOrder.class, result.iterator().next());
    assertEquals("ASSIGNED", ((ProcessedOrder) result.iterator().next()).status());
    verify(ordersRepository, times(1)).findByStatusOrderById("PENDING");
    verify(centersService, times(1)).retrieveAllLogisticsCenters();
  }

  @Test
  void testOrderAssignations_NotProcessedOrder_byType() {
    var order = new Order();
    order.setId(1L);
    order.setSize("m");
    order.setCoordinates(new Coordinates(40.416775, -3.703790));
    order.setStatus("PENDING");

    var center = new Center();
    center.setId(1L);
    center.setName("Center 1");
    center.setCoordinates(new Coordinates(40.416775, -3.703790));
    center.setCurrentLoad(5);
    center.setMaxCapacity(10);
    center.setCapacity("L");

    when(ordersRepository.findByStatusOrderById("PENDING")).thenReturn(List.of(order));
    when(centersService.retrieveAllLogisticsCenters()).thenReturn(List.of(center));

    var result = ordersService.orderAssignations();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertInstanceOf(NotProcessedOrder.class, result.iterator().next());
    assertEquals("No available centers support the order type.",
        ((NotProcessedOrder) result.iterator().next()).message());
    verify(ordersRepository, times(1)).findByStatusOrderById("PENDING");
    verify(centersService, times(1)).retrieveAllLogisticsCenters();
  }

  @Test
  void testOrderAssignations_NotProcessedOrder_byCapacity() {
    var order = new Order();
    order.setId(1L);
    order.setSize("m");
    order.setCoordinates(new Coordinates(40.416775, -3.703790));
    order.setStatus("PENDING");

    var center = new Center();
    center.setId(1L);
    center.setName("Center 1");
    center.setCoordinates(new Coordinates(40.416775, -3.703790));
    center.setCurrentLoad(10);
    center.setMaxCapacity(10);
    center.setCapacity("m");

    when(ordersRepository.findByStatusOrderById("PENDING")).thenReturn(List.of(order));
    when(centersService.retrieveAllLogisticsCenters()).thenReturn(List.of(center));

    var result = ordersService.orderAssignations();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertInstanceOf(NotProcessedOrder.class, result.iterator().next());
    assertEquals("All centers are at maximum capacity.",
        ((NotProcessedOrder) result.iterator().next()).message());
    verify(ordersRepository, times(1)).findByStatusOrderById("PENDING");
    verify(centersService, times(1)).retrieveAllLogisticsCenters();
  }
}