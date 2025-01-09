package com.hackathon.inditex.Services;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Repositories.OrdersRepository;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
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
    return ordersRepository.findByStatusOrderById("PENDING").stream().map(this::processOrder).flatMap(Optional::stream).toList();
  }

  private Optional<Record> processOrder(Order order) {
    var centers = filterCentersThatSupportType(centersService.retrieveAllLogisticsCenters(), order.getSize());
    if (centers.isEmpty()) {
      return Optional.of(NotProcessedOrder.byType(order.getId(), order.getStatus()));
    }

    centers = filterCentersWithEnoughCapacity(centers);
    if (centers.isEmpty()) {
      return Optional.of(NotProcessedOrder.byCapacity(order.getId(), order.getStatus()));
    }

    var availableCenter = sortCentersByDistance(centers, order.getCoordinates());
    return availableCenter.map(center -> assignOrderToCenterAndUpdateItsCapacity(order, center));
  }

  private Collection<Center> filterCentersThatSupportType(Collection<Center> centers, String type) {
    return centers.stream().filter(center -> center.getCapacity().equalsIgnoreCase(type)).toList();
  }

  private Collection<Center> filterCentersWithEnoughCapacity(Collection<Center> centers) {
    return centers.stream().filter(center -> center.getCurrentLoad() < center.getMaxCapacity()).toList();
  }

  private Optional<Center> sortCentersByDistance(Collection<Center> centers, Coordinates coordinates) {
    return centers.stream().min((center1, center2) -> Double.compare(calculateHaversineDistance(coordinates, center1.getCoordinates()), calculateHaversineDistance(coordinates, center2.getCoordinates())));
  }

  private Record assignOrderToCenterAndUpdateItsCapacity(Order order, Center center) {
    order.setAssignedCenter(center.getName());
    order.setStatus("ASSIGNED");
    ordersRepository.save(order);
    centersService.updateDetailsOfAnExistingLogisticsCenter(center.getId(), Map.of("currentLoad", String.valueOf(center.getCurrentLoad() + 1)));
    return new ProcessedOrder(calculateHaversineDistance(order.getCoordinates(), center.getCoordinates()), order.getId(), center.getName());
  }

  private double calculateHaversineDistance(Coordinates start, Coordinates end) {
    final double EARTH_RADIUS = 6371.0;
    double distanceBetweenLatitudes = Math.toRadians(end.getLatitude() - start.getLatitude());
    double distanceBetweenLongitudes = Math.toRadians(end.getLongitude() - start.getLongitude());
    double a = Math.sin(distanceBetweenLatitudes / 2) * Math.sin(distanceBetweenLatitudes / 2) + Math.cos(Math.toRadians(start.getLatitude())) * Math.cos(Math.toRadians(end.getLatitude())) * Math.sin(distanceBetweenLongitudes / 2) * Math.sin(distanceBetweenLongitudes / 2);
    return EARTH_RADIUS * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  }

  public record ProcessedOrder(Double distance, long orderId, String assignedLogisticsCenter, String status) {

    public ProcessedOrder(Double distance, long orderId, String assignedLogisticsCenter) {
      this(distance, orderId, assignedLogisticsCenter, "ASSIGNED");
    }
  }

  public record NotProcessedOrder(Double distance, long orderId, String assignedLogisticsCenter, String message, String status) {

    public static NotProcessedOrder byType(long orderId, String status) {
      return new NotProcessedOrder(null, orderId, null, "No available centers support the order type.", status);
    }

    public static NotProcessedOrder byCapacity(long orderId, String status) {
      return new NotProcessedOrder(null, orderId, null, "All centers are at maximum capacity.", status);
    }
  }
}