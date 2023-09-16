package com.leothenardo.homebroker.orders.application;

import com.leothenardo.homebroker.common.exceptions.ResourceNotFoundException;
import com.leothenardo.homebroker.orders.infra.OrderRepository;
import com.leothenardo.homebroker.orders.model.Order;
import com.leothenardo.homebroker.orders.model.OrderStatus;
import com.leothenardo.homebroker.orders.model.OrderType;
import com.leothenardo.homebroker.orders.model.Transaction;
import com.leothenardo.homebroker.providers.PublisherProvider;
import com.leothenardo.homebroker.wallets.infra.WalletRepository;
import com.leothenardo.homebroker.wallets.model.Wallet;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

@Service
public class OrderService {
	private final WalletRepository walletRepository;
	private final OrderRepository orderRepository;
	private final PublisherProvider publisherProvider;
	private final ReactiveMongoTemplate mongoTemplate;

	public OrderService(WalletRepository walletRepository, OrderRepository orderRepository, PublisherProvider publisherProvider, ReactiveMongoTemplate mongoTemplate) {
		this.walletRepository = walletRepository;
		this.orderRepository = orderRepository;
		this.publisherProvider = publisherProvider;
		this.mongoTemplate = mongoTemplate;
	}

	@Transactional
	public EmitOrderServiceOutputDTO emitOrder(EmitOrderServiceInputDTO input) {
		OrderType type = input.type();
		var order = Order.create(
						input.walletId(),
						type,
						input.assetId(),
						input.price(),
						input.shares()
		);
		Wallet myWallet = this.walletRepository.findById(input.walletId())
						.orElseThrow(() -> new ResourceNotFoundException(input.walletId()));
		int currentShares = myWallet.availableShares(input.assetId());
		if (type == OrderType.SELL) { //validate & compute wallet
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

		this.publisherProvider.publish("input-orders", new OrderEmittedEventDTO(  // TODO: unhardcode queue name
						order.getId(),
						UUID.randomUUID().toString(),
						input.assetId(),
						currentShares,
						input.shares(),
						input.price(),
						input.type()
		));
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
		this.orderRepository.save(buyOrder);
		this.orderRepository.save(sellOrder);
		this.walletRepository.save(buyWallet);
		this.walletRepository.save(sellWallet);
	}

	public SseEmitter subscribe(String walletId) {
		SseEmitter emitter = new SseEmitter(0L);
		ChangeStreamOptions options = ChangeStreamOptions.builder()
						.filter(newAggregation(match(where("wallet_id").is(walletId))))
						.returnFullDocumentOnUpdate()
						.build();

// Use ReactiveMongoTemplate to create a ChangeStreamFlux
		Flux<ChangeStreamEvent<Order>> flux = mongoTemplate.changeStream("orders", options, Order.class);
		flux.subscribe(event -> {
			try {
				if (event.getBody().getStatus() == OrderStatus.FULFILLED) {
					SseEmitter.SseEventBuilder builder = SseEmitter.event()
									.id(event.getTimestamp().toString())
									.name("order-fulfilled")
									.data(new OrderUpdatedEventDTO(
													event.getBody().getId(),
													event.getBody().getAssetId(),
													event.getBody().getShares(),
													event.getBody().getTransactions().get(event.getBody().getTransactions().size() - 1).getShares(),
													event.getBody().getTransactions().get(event.getBody().getTransactions().size() - 1).getPrice(),
													event.getBody().getPrice()
									));
					emitter.send(builder);
				}
				if (event.getBody().getStatus() == OrderStatus.PARTIAL) {
					SseEmitter.SseEventBuilder builder = SseEmitter.event()
									.id(event.getTimestamp().toString())
									.name("order-partial")
									.data(new OrderUpdatedEventDTO(
													event.getBody().getId(),
													event.getBody().getAssetId(),
													event.getBody().getShares(),
													event.getBody().getTransactions().get(event.getBody().getTransactions().size() - 1).getShares(),
													event.getBody().getTransactions().get(event.getBody().getTransactions().size() - 1).getPrice(),
													event.getBody().getPrice()
									));
					emitter.send(builder);
				}
			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		}, emitter::completeWithError, emitter::complete);
		return emitter;
	}
}
