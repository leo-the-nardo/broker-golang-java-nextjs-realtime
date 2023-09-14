package com.leothenardo.homebroker.wallets.application;

import com.leothenardo.homebroker.common.exceptions.ResourceNotFoundException;
import com.leothenardo.homebroker.wallets.dtos.AssetOnWalletDTO;
import com.leothenardo.homebroker.wallets.dtos.CreateWalletOutputDTO;
import com.leothenardo.homebroker.wallets.infra.WalletRepository;
import com.leothenardo.homebroker.wallets.model.AssetID;
import com.leothenardo.homebroker.wallets.model.AssetOnWallet;
import com.leothenardo.homebroker.wallets.model.Wallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
		Map<AssetID, AssetOnWallet> map = this.walletRepository.findById(walletId)
						.map(Wallet::getEmbeddedAssets)
						.orElseThrow(() -> new ResourceNotFoundException(walletId));
		List<AssetOnWalletDTO> toList = map
						.entrySet()
						.stream()
						.map(entry -> new AssetOnWalletDTO(entry.getKey().toString(), entry.getValue().getShares()))
						.collect(Collectors.toList());
		return toList;
	}
}
