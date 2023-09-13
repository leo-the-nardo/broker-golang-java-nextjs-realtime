package com.leothenardo.homebroker.assets.application;

import com.leothenardo.homebroker.assets.dtos.CreateAssetInputDTO;
import com.leothenardo.homebroker.assets.dtos.FindAssetOutputDTO;
import com.leothenardo.homebroker.assets.infra.AssetRepository;
import com.leothenardo.homebroker.assets.model.Asset;
import com.leothenardo.homebroker.common.exceptions.ResourceNotFoundException;
import com.leothenardo.homebroker.wallets.dtos.CreateWalletOutputDTO;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@Service
public class AssetService {
	private final AssetRepository assetRepository;
	private final ReactiveMongoTemplate mongoTemplate;

	public AssetService(AssetRepository assetRepository, ReactiveMongoTemplate mongoTemplate) {
		this.assetRepository = assetRepository;
		this.mongoTemplate = mongoTemplate;
	}

	public FindAssetOutputDTO find(String id) {
		return this.assetRepository.findById(id)
						.map(asset -> new FindAssetOutputDTO(
										asset.getId(),
										asset.getSymbol(),
										asset.getPrice(),
										asset.getCreatedAt(),
										asset.getUpdatedAt()
						))
						.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public CreateWalletOutputDTO create(CreateAssetInputDTO input) {
		return new CreateWalletOutputDTO(this.assetRepository.save(input.toModel()).getId());
	}

	public SseEmitter subscribe() {
		SseEmitter emitter = new SseEmitter(0L);
		Flux<ChangeStreamEvent<Asset>> flux = mongoTemplate.changeStream(Asset.class)
						.watchCollection(Asset.COLLECTION_NAME)
						.listen();
		flux.subscribe(event -> {
			try {
				SseEmitter.SseEventBuilder builder = SseEmitter.event()
								.id(event.getTimestamp().toString())
								.name("message") // default
								.data(event.getBody());
				emitter.send(builder);
			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		}, emitter::completeWithError, emitter::complete);
		return emitter;
	}
}
