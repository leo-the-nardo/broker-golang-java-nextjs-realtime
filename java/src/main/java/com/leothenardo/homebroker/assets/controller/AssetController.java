package com.leothenardo.homebroker.assets.controller;

import com.leothenardo.homebroker.assets.application.AssetNotificationService;
import com.leothenardo.homebroker.assets.application.AssetService;
import com.leothenardo.homebroker.assets.dtos.CreateAssetInputDTO;
import com.leothenardo.homebroker.assets.dtos.CreateAssetOutputDTO;
import com.leothenardo.homebroker.assets.dtos.FindAssetOutputDTO;
import com.leothenardo.homebroker.assets.model.AssetRealtimePoint;
import com.leothenardo.homebroker.assets.model.OneDayCandle;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {
	private final AssetService assetService;
	private final AssetNotificationService assetNotificationService;


	public AssetController(AssetService assetService, AssetNotificationService assetNotificationService) {
		this.assetService = assetService;
		this.assetNotificationService = assetNotificationService;
	}

	@GetMapping
	public ResponseEntity<Page<FindAssetOutputDTO>> findAll(
					@RequestParam(defaultValue = "0") int page,
					@RequestParam(defaultValue = "10") int size,
					@RequestParam(required = false) List<String> notIn
	) {
		return ResponseEntity.ok().body(this.assetService.findAllPaginated(page, size, notIn));
	}

	@GetMapping("/in")
	public ResponseEntity<List<FindAssetOutputDTO>> findAllIn(
					@RequestParam(defaultValue = "0") int page,
					@RequestParam(defaultValue = "10") int size,
					@RequestParam List<String> symbols
	) {
		return ResponseEntity.ok().body(this.assetService.findAllBySymbol(symbols, size, page));
	}


	@GetMapping("/{id}")
	public ResponseEntity<FindAssetOutputDTO> find(@PathVariable String id) {
		return ResponseEntity.ok().body(this.assetService.find(id));
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<CreateAssetOutputDTO> create(@RequestBody CreateAssetInputDTO body) {
		return ResponseEntity.ok().body(this.assetService.create(body));
	}


	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/{symbol}/series")
	public ResponseEntity<List<AssetRealtimePoint>> findLatestBySymbol(@PathVariable String symbol) {
		return ResponseEntity.ok().body(this.assetService.findRecentSeriesBySymbol(symbol));
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/{symbol}/series/events")
	public SseEmitter streamLatestBySymbol(@PathVariable String symbol) {
		return this.assetNotificationService.addSubscriber(symbol);
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/{symbol}/candle")
	public ResponseEntity<List<OneDayCandle>> materializedView(@PathVariable String symbol) {
		return ResponseEntity.ok().body(this.assetService.findLongCandlesBySymbol(symbol));
	}

}

