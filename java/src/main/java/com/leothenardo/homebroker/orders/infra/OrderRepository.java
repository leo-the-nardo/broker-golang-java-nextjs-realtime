package com.leothenardo.homebroker.orders.infra;

import com.leothenardo.homebroker.orders.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
