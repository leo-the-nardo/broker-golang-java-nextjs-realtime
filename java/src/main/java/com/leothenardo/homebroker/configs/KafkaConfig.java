package com.leothenardo.homebroker.configs;

import com.leothenardo.homebroker.assets.listeners.PointReceivedDTO;
import com.leothenardo.homebroker.orders.listeners.MatchReceivedDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {


	@Bean
	public ConsumerFactory<String, PointReceivedDTO> assetConsumerFactory() {
		Map<String, Object> consumerConfig = new HashMap<>();
		consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
		consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "asset");
		consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerConfig.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		consumerConfig.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
		consumerConfig.put(JsonDeserializer.VALUE_DEFAULT_TYPE, PointReceivedDTO.class);
		consumerConfig.put(JsonDeserializer.TRUSTED_PACKAGES, "*");


		return new DefaultKafkaConsumerFactory<>(consumerConfig);
	}

	@Bean
	public ConsumerFactory<String, MatchReceivedDTO> matchConsumerFactory() {
		Map<String, Object> consumerConfig = new HashMap<>();
		consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
		consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "match");
		consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerConfig.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		consumerConfig.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
		consumerConfig.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
						"com.leothenardo.homebroker.orders.listeners.MatchReceivedDTO");
		consumerConfig.put(JsonDeserializer.TRUSTED_PACKAGES, "*");


		return new DefaultKafkaConsumerFactory<>(consumerConfig);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PointReceivedDTO> assetKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, PointReceivedDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(assetConsumerFactory());
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

		return factory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, MatchReceivedDTO> matchKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, MatchReceivedDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(matchConsumerFactory());
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		return factory;
	}
}
