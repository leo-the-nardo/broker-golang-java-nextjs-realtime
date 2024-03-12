package com.leothenardo.homebroker.orders.application;


import com.leothenardo.homebroker._common.exceptions.ResourceNotFoundException;
import com.leothenardo.homebroker._providers.PublisherProvider;
import com.leothenardo.homebroker.orders.dtos.EmitOrderInputDTO;
import com.leothenardo.homebroker.orders.dtos.FetchOrdersOutputDTO;
import com.leothenardo.homebroker.orders.entities.Order;
import com.leothenardo.homebroker.orders.entities.OrderType;
import com.leothenardo.homebroker.orders.entities.Transaction;
import com.leothenardo.homebroker.orders.exceptions.InsufficientAssetsOnWallet;
import com.leothenardo.homebroker.orders.repositories.OrderRepository;
import com.leothenardo.homebroker.users.application.AuthService;
import com.leothenardo.homebroker.wallets.entities.Wallet;
import com.leothenardo.homebroker.wallets.repositories.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderService {
	private final Logger log = LoggerFactory.getLogger(OrderService.class);
	private final WalletRepository walletRepository;
	private final OrderRepository orderRepository;
	private final PublisherProvider publisherProvider;
	private final AuthService authService;

	public OrderService(WalletRepository walletRepository, OrderRepository orderRepository, PublisherProvider publisherProvider, AuthService authService) {
		this.walletRepository = walletRepository;
		this.orderRepository = orderRepository;
		this.publisherProvider = publisherProvider;
		this.authService = authService;
	}

	public FetchOrdersOutputDTO fetchOrders() {
		var walletId = authService.getMe().getWalletId();
		return FetchOrdersOutputDTO.from(this.orderRepository.findAllByWalletIdOrderByCreatedAtDesc(walletId));
	}

	@Transactional
	public EmitOrderServiceOutputDTO emitOrder(EmitOrderInputDTO input) {
		var walletId = authService.getMe().getWalletId();
		OrderType type = input.type();
		var order = Order.create(
						walletId,
						type,
						input.assetId(),
						input.price(),
						input.shares()
		);
		Wallet myWallet = this.walletRepository.findById(walletId)
						.orElseThrow(() -> new ResourceNotFoundException(walletId));
		int currentShares = myWallet.availableShares(input.assetId());
		if (type == OrderType.SELL) { //validate & compute wallet
			boolean ok = myWallet.initSell(input.assetId(), input.shares());
			if (!ok) {
				throw new InsufficientAssetsOnWallet();
			}
			this.orderRepository.save(order);
			this.walletRepository.save(myWallet);
		}
		if (type == OrderType.BUY) {
			this.orderRepository.save(order);
		}
		var message = new OrderEmittedEventDTO(
						order.getId(),
						order.getWalletId(),
						input.assetId(),
						currentShares,
						input.shares(),
						input.price(),
						input.type()
		);
		System.out.println("Emitting order: " + message);
		this.publisherProvider.publish("input-orders", message);
		return EmitOrderServiceOutputDTO.from(order);
	}

	@Transactional
	public void executeTransaction(ExecuteTransactionServiceInputDTO input) {
		Optional<Order> buyOrderOpt = this.orderRepository.findById(input.buyOrderId());
		if (buyOrderOpt.isEmpty()) {
			System.out.println("Buy order not found: " + input.buyOrderId());
			return;
		}
		Order buyOrder = buyOrderOpt.get();
		Wallet buyWallet = this.walletRepository.findById(buyOrder.getWalletId())
						.orElseThrow(() -> new ResourceNotFoundException(buyOrder.getWalletId()));
		Order sellOrder = this.orderRepository.findById(input.sellOrderId())
						.orElseThrow(() -> new ResourceNotFoundException(input.sellOrderId()));
		Wallet sellWallet = this.walletRepository.findById(sellOrder.getWalletId())
						.orElseThrow(() -> new ResourceNotFoundException(sellOrder.getWalletId()));
		sellWallet.computeSell(input.assetId(), input.negotiatedShares(), input.price());
		buyWallet.computeBuy(input.assetId(), input.negotiatedShares(), input.price());
		var transaction = Transaction.create(
						input.brokerTransactionId(),
						input.negotiatedShares(),
						sellOrder.getPrice(),
						input.buyerId(),
						input.sellerId()
		);
		buyOrder.registerTransaction(transaction);
		sellOrder.registerTransaction(transaction);
		try {
			this.orderRepository.save(buyOrder);
			this.orderRepository.save(sellOrder);
			this.walletRepository.save(buyWallet);
			this.walletRepository.save(sellWallet);
		} catch (Exception e) { //TODO: send to dead letter
			System.out.println("Error on execute transaction: " + e.getMessage());
		}
	}

}
