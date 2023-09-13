package com.leothenardo.homebroker.wallets;

import com.leothenardo.homebroker.wallets.application.WalletService;
import com.leothenardo.homebroker.wallets.dtos.CreateWalletOutputDTO;
import com.leothenardo.homebroker.wallets.model.Wallet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {
	private final WalletService walletService;

	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}

	@GetMapping
	public ResponseEntity<List<Wallet>> list() {
		return ResponseEntity.ok().body(this.walletService.list());
	}

	@PostMapping
	public ResponseEntity<CreateWalletOutputDTO> create() {
		CreateWalletOutputDTO id = this.walletService.create();
		return ResponseEntity.ok().body(id);
	}


}
