package com.leothenardo.homebroker.orders.application;

public record ExecuteTransactionServiceInputDTO(
				String buyOrderId,
				String buyerId,
				String sellOrderId,
				String sellerId,
				String brokerTransactionId,
				int negotiatedShares,
				double price,
				String assetId
) {
}
