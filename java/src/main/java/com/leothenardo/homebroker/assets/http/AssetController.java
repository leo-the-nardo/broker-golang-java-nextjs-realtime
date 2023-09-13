package com.leothenardo.homebroker.assets.http;

import com.leothenardo.homebroker.assets.application.AssetService;
import com.leothenardo.homebroker.assets.dtos.CreateAssetInputDTO;
import com.leothenardo.homebroker.assets.dtos.FindAssetOutputDTO;
import com.leothenardo.homebroker.assets.model.Asset;
import com.leothenardo.homebroker.wallets.dtos.CreateWalletOutputDTO;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/assets")
public class AssetController {
	private final AssetService assetService;


	public AssetController(AssetService assetService) {
		this.assetService = assetService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<FindAssetOutputDTO> find(@PathVariable String id) {
		return ResponseEntity.ok().body(this.assetService.find(id));
	}

	@PostMapping
	public ResponseEntity<CreateWalletOutputDTO> create(@RequestBody CreateAssetInputDTO body) {
		return ResponseEntity.ok().body(this.assetService.create(body));
	}

	@GetMapping("/events")
	public SseEmitter streamChanges() {
		return this.assetService.subscribe();
	}
}

