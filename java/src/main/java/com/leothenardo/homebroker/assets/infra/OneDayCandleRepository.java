package com.leothenardo.homebroker.assets.infra;

import com.leothenardo.homebroker.assets.model.OneDayCandle;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface OneDayCandleRepository extends JpaRepository<OneDayCandle, Instant> {

	@Query("SELECT o FROM OneDayCandle o " +
					"WHERE o.symbol = :symbol AND o.bucket >= :startTime " +
					"ORDER BY o.bucket")
	List<OneDayCandle> findCandlesBySymbolAndTime(@Param("symbol") String symbol,
																								@Param("startTime") Instant startTime,
																								PageRequest pageRequest);

	// Optional: Overloaded method without PageRequest to retrieve all data
	@Query("SELECT o FROM OneDayCandle o " +
					"WHERE o.symbol = :symbol AND o.bucket >= :startTime " +
					"ORDER BY o.bucket")
	List<OneDayCandle> findCandlesBySymbolAndTime(@Param("symbol") String symbol,
																								@Param("startTime") Instant startTime);
}