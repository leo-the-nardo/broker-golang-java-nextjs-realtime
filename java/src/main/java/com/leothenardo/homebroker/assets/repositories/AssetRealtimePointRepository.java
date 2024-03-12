package com.leothenardo.homebroker.assets.repositories;

import com.leothenardo.homebroker.assets.model.AssetRealtimePoint;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;


public interface AssetRealtimePointRepository extends JpaRepository<AssetRealtimePoint, Instant> {
	@Query("SELECT s FROM AssetRealtimePoint s " +
					"WHERE s.symbol = :symbol " +
					"AND s.time >= :startTime " +
					"ORDER BY s.time")
	List<AssetRealtimePoint> findLatestBySymbolAndTime(
					@Param("symbol") String symbol,
					@Param("startTime") Instant startTime,
					PageRequest pageRequest);
}
