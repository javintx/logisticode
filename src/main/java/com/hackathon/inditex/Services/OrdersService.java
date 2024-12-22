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

  public Collection<?> orderAssignations() {
    return ordersRepository.findByStatusOrderById("PENDING").stream()
        .map(order -> {
          var availableCenter = getAvailableCenterFor(order);

          if (availableCenter.isPresent()) {
            return assignOrderToCenterAndUpdateItsCapacity(order, availableCenter.get());
          }

          if (thereIsNoCenterWithEnoughCapacity()) {
            return new NotProcessedOrder(order.getId(), order.getStatus(), "All centers are at maximum capacity.");
          }

          return new NotProcessedOrder(order.getId(), order.getStatus(),
              "No available centers support the order type.");

        }).toList();
  }

  private boolean thereIsNoCenterWithEnoughCapacity() {
    return centersService.retrieveAllLogisticsCenters().stream()
        .anyMatch(center -> center.getCurrentLoad() < center.getMaxCapacity());
  }

  private Optional<Center> getAvailableCenterFor(Order order) {
    return centersService.retrieveAllLogisticsCenters().stream()
        .filter(center -> center.getCurrentLoad() < center.getMaxCapacity())
        .filter(center -> center.getCapacity().equals(order.getSize()))
        .min((center1, center2) -> Double.compare(
            calculateHaversineDistance(order.getCoordinates(), center1.getCoordinates()),
            calculateHaversineDistance(order.getCoordinates(), center2.getCoordinates())));
  }

  private ProcessedOrder assignOrderToCenterAndUpdateItsCapacity(Order order, Center center) {
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

  public record ProcessedOrder(Double distance, long id, String assignedLogisticsCenter, String status) {

    public ProcessedOrder(Double distance, long id, String assignedLogisticsCenter) {
      this(distance, id, assignedLogisticsCenter, "ASSIGNED");
    }
  }

  public record NotProcessedOrder(Double distance, long id, String assignedLogisticsCenter,
                                  String message, String status) {

    public NotProcessedOrder(long id, String status, String message) {
      this(null, id, null, message, status);
    }
  }
}