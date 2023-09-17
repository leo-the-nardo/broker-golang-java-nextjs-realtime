package com.leothenardo.homebroker.assets.dtos;

import java.time.Instant;

public record AssetPointComputedEvent(
				String symbol,
				Float price,
				Instant time
) {
}
