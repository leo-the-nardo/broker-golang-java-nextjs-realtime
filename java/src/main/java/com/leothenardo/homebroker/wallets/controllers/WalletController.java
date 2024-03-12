package com.leothenardo.homebroker.wallets.controllers;

import com.leothenardo.homebroker.wallets.application.WalletService;
import com.leothenardo.homebroker.wallets.dtos.AssetOnWalletDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {
	private final WalletService walletService;

	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/assets")
	public ResponseEntity<List<AssetOnWalletDTO>> listAssets() {
		return ResponseEntity.ok().body(this.walletService.listAssets());
	}


	@ApiResponse(content = {@Content(
					mediaType = "text/event-stream",
					schema = @Schema(implementation = AssetOnWalletDTO.class)
	)})
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/assets/events")
	public SseEmitter streamChanges() {
		return this.walletService.subscribe();
	}
}
