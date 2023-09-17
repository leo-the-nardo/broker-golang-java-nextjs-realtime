package com.leothenardo.homebroker.assets.application;

import com.leothenardo.homebroker.assets.dtos.AssetPointComputedEvent;
import com.leothenardo.homebroker.assets.listeners.AssetListener;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {
	private final static Logger log = LoggerFactory.getLogger(NotificationService.class);
	//simbol -> user -> emitter
	private final Map<String, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

	public SseEmitter addSubscriber(String symbol) {
		var id = Instant.now().toString();
		SseEmitter emitter = new SseEmitter(0L);
		var exists = this.emitters.containsKey(symbol);
		if (exists) {
			var newEmitters = this.emitters.get(symbol);
			newEmitters.put(id, emitter);
			this.emitters.put(symbol, newEmitters);
			return emitter;
		}
		var mapUserEmitter = new ConcurrentHashMap<String, SseEmitter>();
		mapUserEmitter.put(id, emitter);
		this.emitters.put(symbol, mapUserEmitter);

		return emitter;
	}

	public void removeSubscriber(String symbol, String id) {
		var newEmitters = this.emitters.get(symbol);
		newEmitters.remove(id);
		this.emitters.put(symbol, newEmitters);
	}

	public void sendToSubscribers(String symbol, AssetPointComputedEvent data) {
		var exists = this.emitters.containsKey(symbol);
		if (!exists) {
			return;
		}
		for (Map.Entry<String, SseEmitter> entry : this.emitters.get(symbol).entrySet()) {
			try {
				SseEmitter.SseEventBuilder builder = SseEmitter.event()
								.id(symbol)
								.name("asset-updated")
								.data(data);
				entry.getValue().send(builder);
			} catch (Exception e) {
				removeSubscriber(symbol, entry.getKey());
			}
		}

	}
}
