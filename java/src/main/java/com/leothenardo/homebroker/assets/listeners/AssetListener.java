package com.leothenardo.homebroker.assets.listeners;

import com.leothenardo.homebroker.assets.application.AssetService;
import com.leothenardo.homebroker.orders.application.ExecuteTransactionServiceInputDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AssetListener {
	private final static Logger log = LoggerFactory.getLogger(AssetListener.class);
	private final AssetService assetService;

	public AssetListener(AssetService assetService) {
		this.assetService = assetService;
	}

	@KafkaListener(topics = "asset", groupId = "asset", containerFactory = "assetKafkaListenerContainerFactory")
	public void consume(@Payload PointReceivedDTO pointReceived, Acknowledgment ack) {
		System.out.println("Point received: " + pointReceived);
		try {
			assetService.computePoint(pointReceived.getSymbol(), pointReceived.getPrice(), pointReceived.getShares());
			ack.acknowledge();

		} catch (Exception e) { // TODO: send to DLQ
			log.error(e.getMessage(), e);
		}
	}
}
