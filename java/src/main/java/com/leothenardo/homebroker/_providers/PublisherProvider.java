package com.leothenardo.homebroker._providers;

public interface PublisherProvider {
	void publish(String topic, Object message);
}
