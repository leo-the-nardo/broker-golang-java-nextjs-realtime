package com.leothenardo.homebroker.orders.exceptions;

public class InsufficientAssetsOnWallet extends RuntimeException {
	public InsufficientAssetsOnWallet() {
		super("Você não possui ativos suficientes para realizar esta operação.");
	}
}
