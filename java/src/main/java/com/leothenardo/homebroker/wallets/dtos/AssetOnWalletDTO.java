package com.leothenardo.homebroker.wallets.dtos;

import com.leothenardo.homebroker.wallets.model.Wallet;

public record AssetOnWalletDTO(
				String assetId,
				int shares,
				int preTakenShares
) {
	public static AssetOnWalletDTO from(Wallet.Asset asset) {
		return new AssetOnWalletDTO(
						asset.getAssetId(),
						asset.getAvailableShares(),
						asset.getPreTakenShares()
		);
	}
}
