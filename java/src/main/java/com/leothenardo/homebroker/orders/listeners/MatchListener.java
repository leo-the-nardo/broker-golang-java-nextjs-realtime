package com.leothenardo.homebroker.orders.listeners;

import com.leothenardo.homebroker.orders.application.ExecuteTransactionServiceInputDTO;
import com.leothenardo.homebroker.orders.application.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MatchListener {
	private final OrderService orderService;

	public MatchListener(OrderService orderService) {
		this.orderService = orderService;
	}

	@KafkaListener(topics = "match", groupId = "orders")
	public void consume(@Payload MatchReceivedDTO matchReceived, Acknowledgment ack) {
		System.out.println("Match received: " + matchReceived);
		try {
			orderService.executeTransaction(new ExecuteTransactionServiceInputDTO(
							matchReceived.getBuy_order().getOrder_id(),
							matchReceived.getBuy_order().getInvestor_id(),
							matchReceived.getSell_order().getOrder_id(),
							matchReceived.getSell_order().getInvestor_id(),
							matchReceived.getTransaction_id(),
							matchReceived.getShares(),
							matchReceived.getPrice(),
							matchReceived.getAsset_id()));
			ack.acknowledge();
			
		} catch (Exception e) { // TODO: send to DLQ
			System.out.println("Error: " + e.getMessage());
		}
	}
}
