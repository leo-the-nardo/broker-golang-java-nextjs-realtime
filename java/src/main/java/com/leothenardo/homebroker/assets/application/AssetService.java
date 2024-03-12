package com.leothenardo.homebroker.assets.application;

import com.leothenardo.homebroker._common.exceptions.ResourceNotFoundException;
import com.leothenardo.homebroker.assets.dtos.AssetPointComputedEvent;
import com.leothenardo.homebroker.assets.dtos.CreateAssetInputDTO;
import com.leothenardo.homebroker.assets.dtos.CreateAssetOutputDTO;
import com.leothenardo.homebroker.assets.dtos.FindAssetOutputDTO;
import com.leothenardo.homebroker.assets.model.Asset;
import com.leothenardo.homebroker.assets.model.AssetRealtimePoint;
import com.leothenardo.homebroker.assets.model.OneDayCandle;
import com.leothenardo.homebroker.assets.repositories.AssetRealtimePointRepository;
import com.leothenardo.homebroker.assets.repositories.AssetRepository;
import com.leothenardo.homebroker.assets.repositories.OneDayCandleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AssetService {
	private final AssetRepository assetRepository;
	private final AssetRealtimePointRepository assetRealtimePointRepository;
	private final OneDayCandleRepository oneDayCandleRepository;
	private final AssetNotificationService assetNotificationService;

	public AssetService(AssetRepository assetRepository, AssetRealtimePointRepository assetRealtimePointRepository, OneDayCandleRepository oneDayCandleRepository, AssetNotificationService assetNotificationService) {
		this.assetRepository = assetRepository;
		this.assetRealtimePointRepository = assetRealtimePointRepository;
		this.oneDayCandleRepository = oneDayCandleRepository;
		this.assetNotificationService = assetNotificationService;
	}

	public FindAssetOutputDTO find(String symbol) {
		return this.assetRepository.findById(symbol)
						.map(asset -> new FindAssetOutputDTO(
										asset.getSymbol(),
										asset.getName()
						))
						.orElseThrow(() -> new ResourceNotFoundException(symbol));
	}

	public CreateAssetOutputDTO create(CreateAssetInputDTO input) {
		return new CreateAssetOutputDTO(this.assetRepository.save(Asset.create(input.symbol(), input.name())).getSymbol());
	}


	public List<AssetRealtimePoint> findRecentSeriesBySymbol(String symbol) {
		final var startTime = Instant.now().minusSeconds(24 * 60 * 60); //last 24h
		final var pageRequest = PageRequest.of(0, 99999);
		return assetRealtimePointRepository.findLatestBySymbolAndTime(symbol, startTime, pageRequest);
	}

	public List<OneDayCandle> findLongCandlesBySymbol(String symbol) {
		final var startTime = Instant.now().minusSeconds(70 * 24 * 60 * 60); // 70 days ago
		final var pageRequest = PageRequest.of(0, 100);
		return oneDayCandleRepository.findCandlesBySymbolAndTime(symbol, startTime, pageRequest);
	}

	public void computePoint(String symbol, Float price, int shares) {
		AssetRealtimePoint point = AssetRealtimePoint.create(symbol, price, shares);
		this.assetRealtimePointRepository.save(point);
		System.out.println(point);
		this.assetNotificationService.sendToSubscribers(symbol, new AssetPointComputedEvent(
						symbol,
						price,
						point.getTime()
		));
	}

	public Page<FindAssetOutputDTO> findAllPaginated(int page, int size, List<String> notIn) {
		Pageable pageRequest = PageRequest.of(page, size);
		if (notIn == null) {
			return this.assetRepository.findAll(pageRequest)
							.map(asset -> new FindAssetOutputDTO(
											asset.getSymbol(),
											asset.getName()
							));
		}
		return this.assetRepository.findAllNotIn(notIn, pageRequest)
						.map(asset -> new FindAssetOutputDTO(
										asset.getSymbol(),
										asset.getName()
						));
	}

	public List<FindAssetOutputDTO> findAllBySymbol(List<String> symbols, int size, int page) {
		Pageable pageRequest = PageRequest.of(page, size);

		return this.assetRepository.findBySymbolIn(symbols).stream().map(asset -> new FindAssetOutputDTO(
						asset.getSymbol(),
						asset.getName()
		)).toList();
	}

}
