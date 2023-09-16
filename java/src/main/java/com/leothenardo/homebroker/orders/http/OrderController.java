package com.leothenardo.homebroker.orders.http;

import com.leothenardo.homebroker.orders.application.EmitOrderServiceInputDTO;
import com.leothenardo.homebroker.orders.application.EmitOrderServiceOutputDTO;
import com.leothenardo.homebroker.orders.application.OrderService;
import com.leothenardo.homebroker.orders.dtos.EmitOrderInputDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/wallets/{walletId}/orders")
public class OrderController {
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<EmitOrderServiceOutputDTO> emitOrder(
					@PathVariable("walletId") String walletId,
					@RequestBody @Valid EmitOrderInputDTO body
	) {
		return ResponseEntity.ok().body(this.orderService.emitOrder(new EmitOrderServiceInputDTO(
										walletId,
										body.assetId(),
										body.shares(),
										body.type(),
										body.price()
						)
		));
	}

	@GetMapping("/events")
	public SseEmitter streamChanges(
					@PathVariable("walletId") String walletId
	) {
		return this.orderService.subscribe(walletId);
	}

}
