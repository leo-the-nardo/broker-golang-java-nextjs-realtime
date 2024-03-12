package com.leothenardo.homebroker.orders.application;

import com.leothenardo.homebroker.orders.dtos.OrderUpdatedEventDTO;
import com.leothenardo.homebroker.orders.entities.Order;
import com.leothenardo.homebroker.orders.entities.OrderStatus;
import com.leothenardo.homebroker.users.application.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class OrderNotificationService {
	private final Logger log = LoggerFactory.getLogger(OrderService.class);
	private final AuthService authService;
	private final ReactiveMongoTemplate mongoTemplate;

	public OrderNotificationService(AuthService authService, ReactiveMongoTemplate mongoTemplate) {
		this.authService = authService;
		this.mongoTemplate = mongoTemplate;
	}


	public SseEmitter subscribe() {
		var walletId = authService.getMe().getWalletId();
		SseEmitter emitter = new SseEmitter(0L);
		try {
			emitter.send(SseEmitter.event().name("connected").data("Connected"));
		} catch (Exception e) {
			log.error("Error on send connected event order", e);
			emitter.completeWithError(e);
			return emitter;
		}
		ChangeStreamOptions options = ChangeStreamOptions.builder()
						.filter(newAggregation(match(where("wallet_id").is(walletId))))
						.returnFullDocumentOnUpdate()
						.build();

		Flux<ChangeStreamEvent<Order>> flux = mongoTemplate.changeStream(Order.COLLECTION_NAME, options, Order.class);
		Disposable disposable = flux.subscribe(event -> {
			try {
				if (event.getBody().getStatus() == OrderStatus.FULFILLED) {
					SseEmitter.SseEventBuilder builder = SseEmitter.event()
									.id(event.getTimestamp().toString())
									.name("order-fulfilled")
									.data(OrderUpdatedEventDTO.from(event.getBody()));
					emitter.send(builder);
				}
				if (event.getBody().getStatus() == OrderStatus.PARTIAL) {
					SseEmitter.SseEventBuilder builder = SseEmitter.event()
									.id(event.getTimestamp().toString())
									.name("order-partial")
									.data(OrderUpdatedEventDTO.from(event.getBody()));
					emitter.send(builder);
				}
			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		}, emitter::completeWithError, emitter::complete);
		emitter.onCompletion(disposable::dispose);
		emitter.onError(e -> disposable.dispose());
		return emitter;
	}
}
