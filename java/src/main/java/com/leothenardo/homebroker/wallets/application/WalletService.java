package com.leothenardo.homebroker.wallets.application;

import com.leothenardo.homebroker.common.exceptions.ResourceNotFoundException;
import com.leothenardo.homebroker.wallets.dtos.AssetOnWalletDTO;
import com.leothenardo.homebroker.wallets.dtos.CreateWalletOutputDTO;
import com.leothenardo.homebroker.wallets.infra.WalletRepository;
import com.leothenardo.homebroker.wallets.model.Wallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

	@Transactional(readOnly = true)
	public List<AssetOnWalletDTO> listAssets(String walletId) {
		List<Wallet.Asset> assets = this.walletRepository.findById(walletId)
						.map(Wallet::getAssets)
						.orElseThrow(() -> new ResourceNotFoundException(walletId));

		return assets.stream().map(AssetOnWalletDTO::from).toList();

	}

}
