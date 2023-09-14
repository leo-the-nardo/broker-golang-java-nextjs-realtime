package com.leothenardo.homebroker.providers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class KafkaPublisherProvider implements PublisherProvider {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public KafkaPublisherProvider(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public void publish(String topic, Object message) {
		Objects.requireNonNull(topic);
		Objects.requireNonNull(message);
		kafkaTemplate.send(topic, message);

	}
}
