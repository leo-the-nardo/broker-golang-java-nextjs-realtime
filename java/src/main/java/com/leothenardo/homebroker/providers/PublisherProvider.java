package com.leothenardo.homebroker.providers;

public interface PublisherProvider {
	void publish(String topic, Object message);
}
