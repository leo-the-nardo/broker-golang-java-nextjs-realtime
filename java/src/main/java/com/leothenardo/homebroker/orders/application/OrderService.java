package com.leothenardo.homebroker.orders.application;

import com.leothenardo.homebroker.common.exceptions.ResourceNotFoundException;
import com.leothenardo.homebroker.orders.infra.OrderRepository;
import com.leothenardo.homebroker.orders.model.AssetOnOrder;
import com.leothenardo.homebroker.orders.model.Order;
import com.leothenardo.homebroker.orders.model.OrderType;
import com.leothenardo.homebroker.providers.PublisherProvider;
import com.leothenardo.homebroker.wallets.infra.WalletRepository;
import com.leothenardo.homebroker.wallets.model.Wallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
	private final WalletRepository walletRepository;
	private final OrderRepository orderRepository;
	private final PublisherProvider publisherProvider;

	public OrderService(WalletRepository walletRepository, OrderRepository orderRepository, PublisherProvider publisherProvider) {
		this.walletRepository = walletRepository;
		this.orderRepository = orderRepository;
		this.publisherProvider = publisherProvider;
	}

	@Transactional
	public EmitOrderServiceOutputDTO emitOrder(EmitOrderServiceInputDTO input) {
		OrderType type = input.type();
		var order = Order.create(
						input.walletId(),
						type,
						new AssetOnOrder(
										input.assetId(),
										input.price(),
										input.shares()
						)
		);
		if (type == OrderType.SELL) { //validate & compute wallet
			Wallet myWallet = this.walletRepository.findById(input.walletId())
							.orElseThrow(() -> new ResourceNotFoundException(input.walletId()));
			boolean ok = myWallet.initSell(input.assetId(), input.shares());
			if (!ok) {
				throw new IllegalStateException("not able to sell");
			}
			this.orderRepository.save(order);
			this.walletRepository.save(myWallet);
		}
		if (type == OrderType.BUY) {
			this.orderRepository.save(order);
		}
		// TODO: hardcoded queue name
		this.publisherProvider.publish("input-orders", new OrderEmittedEventDTO(
						order.getId(),
						"TODO",
						input.assetId(),
						input.shares(),
						input.price(),
						input.type()
		));
		return EmitOrderServiceOutputDTO.from(order);
	}


}
