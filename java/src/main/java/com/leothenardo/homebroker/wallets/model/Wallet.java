package com.leothenardo.homebroker.wallets.model;

import com.leothenardo.homebroker.assets.model.Asset;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.*;

@Document(Wallet.COLLECTION_NAME)
public class Wallet {
	public static final String COLLECTION_NAME = "wallets";
	@Id
	@Field(value = "_id", targetType = FieldType.STRING)
	private String id;

	@CreatedDate
	@Field("created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Field("updated_at")
	private LocalDateTime updatedAt;

	@Field("assets")
	private Map<AssetID, AssetOnWallet> walletAssets = new HashMap<>();

	public Wallet() {
	}

	public Wallet(String id, LocalDateTime createdAt, LocalDateTime updatedAt, Map<AssetID, AssetOnWallet> walletAssets) {
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.walletAssets = walletAssets;
	}

	public static Wallet create() {
		return new Wallet(
						UUID.randomUUID().toString(),
						LocalDateTime.now(),
						LocalDateTime.now(),
						new HashMap<>()
		);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Map<AssetID, AssetOnWallet> getEmbeddedAssets() {
		return Collections.unmodifiableMap(this.walletAssets);
	}

	public void addShares(Asset asset, int shares) {
		if (shares < 0) throw new IllegalArgumentException("Shares must be positive");
		var assetId = new AssetID(asset.getId());
		if (!this.walletAssets.containsKey(assetId)) {
			this.walletAssets.put(assetId, new AssetOnWallet(shares));
			return;
		}
		AssetOnWallet assetOnWallet = this.walletAssets.get(assetId);
		assetOnWallet.setShares(assetOnWallet.getShares() + shares);
		this.walletAssets.put(assetId, assetOnWallet);
	}

	public void removeShares(Asset asset, int shares) {
		if (shares < 0) throw new IllegalArgumentException("Shares must be positive");
		var assetId = new AssetID(asset.getId());
		if (!this.walletAssets.containsKey(assetId)) {
			throw new IllegalArgumentException("This asset is not on this wallet");
		}
		AssetOnWallet assetOnWallet = this.walletAssets.get(assetId);
		assetOnWallet.setShares(assetOnWallet.getShares() - shares);
		this.walletAssets.put(assetId, assetOnWallet);

	}

	private boolean validateIsAbleToSellAsset(AssetID assetId, int shares) {
		if (!walletAssets.containsKey(assetId)) {
			return false;
//			throw new IllegalStateException("Fraudulent order, trying to sell an asset that does not belong to the wallet.");
		}
		var assetOnWallet = walletAssets.get(assetId);
		if (assetOnWallet.getShares() < shares) {
			return false;
//			throw new IllegalStateException("Fraudulent order, trying to sell more shares than the wallet has.");
		}
		return true;
	}

	public boolean initSell(String assetToSell, int shares) {
		var assetId = new AssetID(assetToSell);
		boolean isValid = this.validateIsAbleToSellAsset(assetId, shares);
		if (!isValid) return false;
		var actualShares = this.walletAssets.get(assetId).getShares();
		this.walletAssets.put(assetId, new AssetOnWallet(actualShares - shares));
		return true;
	}
}

