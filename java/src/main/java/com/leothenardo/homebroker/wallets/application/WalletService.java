package com.leothenardo.homebroker.wallets.application;

import com.leothenardo.homebroker.wallets.dtos.CreateWalletOutputDTO;
import com.leothenardo.homebroker.wallets.infra.WalletRepository;
import com.leothenardo.homebroker.wallets.model.Wallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WalletService {
	private final WalletRepository walletRepository;

	public WalletService(WalletRepository walletRepository) {
		this.walletRepository = walletRepository;
	}

	@Transactional
	public CreateWalletOutputDTO create() {
		var createdWallet = Wallet.create();
		this.walletRepository.save(createdWallet);
		return new CreateWalletOutputDTO(createdWallet.getId());
	}

	public List<Wallet> list() {
		return this.walletRepository.findAll();
	}
}
