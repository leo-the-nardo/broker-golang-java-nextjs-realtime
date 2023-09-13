package com.leothenardo.homebroker.wallets.model;

public class AssetOnWallet {
	private int shares;

	public AssetOnWallet(int shares) {
		this.shares = shares;
	}

	public int getShares() {
		return this.shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}
}
