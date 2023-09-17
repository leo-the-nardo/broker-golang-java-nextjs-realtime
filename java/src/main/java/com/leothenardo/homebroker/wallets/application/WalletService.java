package com.leothenardo.homebroker.wallets.application;

import com.leothenardo.homebroker.common.exceptions.ResourceNotFoundException;
import com.leothenardo.homebroker.wallets.dtos.AssetOnWalletDTO;
import com.leothenardo.homebroker.wallets.dtos.CreateWalletOutputDTO;
import com.leothenardo.homebroker.wallets.infra.WalletRepository;
import com.leothenardo.homebroker.wallets.model.Wallet;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class WalletService {
	private final WalletRepository walletRepository;
	private final ReactiveMongoTemplate mongoTemplate;

	public WalletService(WalletRepository walletRepository, ReactiveMongoTemplate mongoTemplate) {
		this.walletRepository = walletRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Transactional
	public CreateWalletOutputDTO create() {
		var createdWallet = Wallet.create();
		this.walletRepository.save(createdWallet);
		return new CreateWalletOutputDTO(createdWallet.getId());
	}

	@Transactional(readOnly = true)
	public List<AssetOnWalletDTO> listAssets(String walletId) {
		List<Wallet.Asset> assets = this.walletRepository.findById(walletId)
						.map(Wallet::getAssets)
						.orElseThrow(() -> new ResourceNotFoundException(walletId));

		return assets.stream().map(AssetOnWalletDTO::from).toList();

	}

	public SseEmitter subscribe(String walletId) {
		SseEmitter emitter = new SseEmitter(0L);
		ChangeStreamOptions options = ChangeStreamOptions.builder()
						.filter(newAggregation(match(where("_id").is(walletId))))
						.returnFullDocumentOnUpdate()
						.build();

		Flux<ChangeStreamEvent<Wallet>> flux = mongoTemplate.changeStream(Wallet.COLLECTION_NAME, options, Wallet.class);
		flux.subscribe(event -> {
			try {
				SseEmitter.SseEventBuilder builder = SseEmitter.event()
								.id(event.getTimestamp().toString())
								.name("wallet-asset-updated")
								.data(event.getBody().getAssets().stream().map(AssetOnWalletDTO::from).toList());
				emitter.send(builder);
			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		}, emitter::completeWithError, emitter::complete);
		return emitter;
	}

}
