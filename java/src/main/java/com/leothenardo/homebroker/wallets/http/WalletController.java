package com.leothenardo.homebroker.wallets.http;

import com.leothenardo.homebroker.wallets.application.WalletService;
import com.leothenardo.homebroker.wallets.dtos.AssetOnWalletDTO;
import com.leothenardo.homebroker.wallets.dtos.CreateWalletOutputDTO;
import com.leothenardo.homebroker.wallets.model.Wallet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {
	private final WalletService walletService;

	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}

	@PostMapping
	public ResponseEntity<CreateWalletOutputDTO> create() {
		CreateWalletOutputDTO id = this.walletService.create();
		return ResponseEntity.ok().body(id);
	}

	@GetMapping("/{id}/assets")
	public ResponseEntity<List<AssetOnWalletDTO>> listAssets(@PathVariable("id") String id) {
		return ResponseEntity.ok().body(this.walletService.listAssets(id));
	}
	

}
