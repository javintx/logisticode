package com.hackathon.inditex.Repositories;

import com.hackathon.inditex.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Order, Long> {

}