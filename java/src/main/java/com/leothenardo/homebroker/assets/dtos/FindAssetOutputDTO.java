package com.leothenardo.homebroker.assets.dtos;

import java.time.LocalDateTime;

public record FindAssetOutputDTO(
				String id,
				String symbol,
				Float price,
				LocalDateTime createdAt,
				LocalDateTime updatedAt
) {
}