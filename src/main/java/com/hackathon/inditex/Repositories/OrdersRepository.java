package com.hackathon.inditex.Repositories;

import com.hackathon.inditex.Entities.Order;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Order, Long> {

  Collection<Order> findByStatusOrderById(String status);
}