package com.leothenardo.homebroker.orders.repositories;

import com.leothenardo.homebroker.orders.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

	List<Order> findAllByWalletIdOrderByCreatedAtDesc(String walletId);

}
