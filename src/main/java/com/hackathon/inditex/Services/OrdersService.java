package com.hackathon.inditex.Services;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Repositories.OrdersRepository;
import com.hackathon.inditex.ValueObjects.NotProcessedOrder;
import com.hackathon.inditex.ValueObjects.ProcessedOrder;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

  private final OrdersRepository ordersRepository;
  private final CentersService centersService;

  @Autowired
  public OrdersService(OrdersRepository ordersRepository, CentersService centersService) {
    this.ordersRepository = ordersRepository;
    this.centersService = centersService;
  }

  public Order create(Order order) {
    order.setStatus("PENDING");
    return ordersRepository.save(order);
  }

  public Collection<Order> getAllOrders() {
    return ordersRepository.findAll();
  }

  public Collection<Record> orderAssignations() {
    return ordersRepository.findByStatusOrderById("PENDING").stream()
        .map(this::processOrder)
        .toList();
  }

  private Record processOrder(Order order) {
    var centers = filterCentersThatSupportType(centersService.retrieveAllLogisticsCenters(), order.getSize());
    if (centers.isEmpty()) {
      return NotProcessedOrder.byType(order.getId(), order.getStatus());
    }

    centers = filterCentersWithEnoughCapacity(centers);
    if (centers.isEmpty()) {
      return NotProcessedOrder.byCapacity(order.getId(), order.getStatus());
    }

    var availableCenters = sortCentersByDistance(centers, order.getCoordinates());
    return assignOrderToCenterAndUpdateItsCapacity(order, availableCenters.iterator().next());
  }

  private Collection<Center> filterCentersThatSupportType(Collection<Center> centers, String type) {
    return centers.stream()
        .filter(center -> center.getCapacity().equalsIgnoreCase(type))
        .toList();
  }

  private Collection<Center> filterCentersWithEnoughCapacity(Collection<Center> centers) {
    return centers.stream()
        .filter(center -> center.getCurrentLoad() < center.getMaxCapacity())
        .toList();
  }

  private Collection<Center> sortCentersByDistance(Collection<Center> centers, Coordinates coordinates) {
    return centers.stream()
        .sorted(Comparator.comparingDouble(center -> calculateHaversineDistance(coordinates, center.getCoordinates())))
        .toList();
  }

  private Record assignOrderToCenterAndUpdateItsCapacity(Order order, Center center) {
    order.setAssignedCenter(center.getName());
    order.setStatus("ASSIGNED");
    ordersRepository.save(order);
    centersService.updateDetailsOfAnExistingLogisticsCenter(center.getId(),
        Map.of("currentLoad", String.valueOf(center.getCurrentLoad() + 1)));
    return new ProcessedOrder(calculateHaversineDistance(order.getCoordinates(), center.getCoordinates()),
        order.getId(), center.getName());
  }

  private double calculateHaversineDistance(Coordinates start, Coordinates end) {
    final double EARTH_RADIUS = 6371.0;
    double distanceBetweenLatitudes = Math.toRadians(end.getLatitude() - start.getLatitude());
    double distanceBetweenLongitudes = Math.toRadians(end.getLongitude() - start.getLongitude());
    double a = Math.sin(distanceBetweenLatitudes / 2)
        * Math.sin(distanceBetweenLatitudes / 2)
        + Math.cos(Math.toRadians(start.getLatitude()))
        * Math.cos(Math.toRadians(end.getLatitude()))
        * Math.sin(distanceBetweenLongitudes / 2)
        * Math.sin(distanceBetweenLongitudes / 2);
    return EARTH_RADIUS * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  }

}