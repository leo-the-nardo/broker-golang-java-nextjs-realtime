package com.leothenardo.homebroker.wallets.model;

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
	private List<Asset> walletAssets = new ArrayList<>();

	public Wallet() {
	}

	public Wallet(String id, LocalDateTime createdAt, LocalDateTime updatedAt, List<Asset> walletAssets) {
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
						new ArrayList<>()
		);
	}

	public String getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}


	public List<Asset> getAssets() {
		return Collections.unmodifiableList(this.walletAssets);
	}

	public void addShares(String assetId, int shares) {
		if (shares < 0) throw new IllegalArgumentException("Shares must be positive");
		var indexOfAsset = getIndexAssetOnWalletById(assetId);
		if (indexOfAsset == -1) {
			this.walletAssets.add(Asset.create(assetId, shares));
			return;
		}
		Asset assetOnWallet = this.walletAssets.get(indexOfAsset);
		assetOnWallet.addShares(shares);
		this.walletAssets.set(indexOfAsset, assetOnWallet);
	}

	private boolean validateIsAbleToSellAsset(String assetId, int shares) {
		var assetOnWallet = this.getAssetOnWalletById(assetId);
		if (assetOnWallet.isEmpty()) {
			return false;
		}
		if (assetOnWallet.get().getAvailableShares() < shares) {
			return false;
		}
		return true;
	}

	public int availableShares(String assetId) {
		Optional<Asset> asset = this.getAssetOnWalletById(assetId);
		return asset.map(Asset::getAvailableShares).orElse(0);
	}

	public boolean initSell(String assetId, int shares) {
		boolean isValid = this.validateIsAbleToSellAsset(assetId, shares);
		if (!isValid) return false;
		int indexToUpdate = getIndexAssetOnWalletById(assetId); // equals only compares assetId
		var assetOnWallet = this.walletAssets.get(indexToUpdate);
		assetOnWallet.preTakeShares(shares);
		this.walletAssets.set(indexToUpdate, assetOnWallet);

		return true;
	}


	public void computeSell(String assetId, int sharesSold, double price) {
		int indexToUpdate = getIndexAssetOnWalletById(assetId);
		if (indexToUpdate == -1) {
			throw new IllegalStateException("Fraudulent order, trying to buy an asset that does not belong to the wallet.");
		}
		var assetOnWallet = this.walletAssets.get(indexToUpdate);
		assetOnWallet.removeShares(sharesSold);
		this.walletAssets.set(indexToUpdate, assetOnWallet);
	}

	public void computeBuy(String assetId, int shares, double price) {
		this.addShares(assetId, shares);
	}

	private Optional<Asset> getAssetOnWalletById(String assetId) {
		Asset assetOnWallet = new Asset(assetId, 0, 0);
		int i = this.walletAssets.indexOf(assetOnWallet);
		if (i == -1) {
			return Optional.empty();
		}
		return Optional.of(this.walletAssets.get(i));
	}

	private int getIndexAssetOnWalletById(String assetId) {
		Asset assetOnWallet = new Asset(assetId, 0, 0);
		return this.walletAssets.indexOf(assetOnWallet);
	}


	public static class Asset {
		private String assetId;
		private int shares;
		private int preTakenShares; // e.g of use : sell emited but not matched yet

		public Asset() {
		}

		public Asset(String assetId, int shares, int preTakenShares) {
			this.assetId = assetId;
			this.shares = shares;
			this.preTakenShares = preTakenShares;
		}

		private static Asset create(String assetId, int shares) {
			return new Asset(assetId, shares, 0);
		}

		public int getAvailableShares() {
			return this.shares - this.preTakenShares;
		}

		public int getPreTakenShares() {
			return preTakenShares;
		}

		private void preTakeShares(int sharesToTake) {
			this.preTakenShares += sharesToTake;
		}

		private void addShares(int sharesToAdd) {
			this.shares += sharesToAdd;
		}

		public String getAssetId() {
			return assetId;
		}

		private void setAssetId(String assetId) {
			this.assetId = assetId;
		}


		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Asset that = (Asset) o;
			return Objects.equals(assetId, that.assetId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(assetId);
		}


		public void removeShares(int shares) {
			this.preTakenShares -= shares;
		}
	}

}

