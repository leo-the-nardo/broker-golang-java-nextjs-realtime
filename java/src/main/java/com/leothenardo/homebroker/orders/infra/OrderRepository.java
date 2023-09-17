package com.leothenardo.homebroker.orders.infra;

import com.leothenardo.homebroker.orders.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

	List<Order> findAllByWalletIdOrderByCreatedAtDesc(String walletId);

}
