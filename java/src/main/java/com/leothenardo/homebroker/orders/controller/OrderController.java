package com.leothenardo.homebroker.orders.controller;

import com.leothenardo.homebroker.orders.application.EmitOrderServiceOutputDTO;
import com.leothenardo.homebroker.orders.application.OrderNotificationService;
import com.leothenardo.homebroker.orders.application.OrderService;
import com.leothenardo.homebroker.orders.dtos.EmitOrderInputDTO;
import com.leothenardo.homebroker.orders.dtos.FetchOrdersOutputDTO;
import com.leothenardo.homebroker.orders.dtos.OrderUpdatedEventDTO;
import com.leothenardo.homebroker.orders.exceptions.InsufficientAssetsOnWallet;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
	private final OrderService orderService;
	private final OrderNotificationService orderNotificationService;

	public OrderController(OrderService orderService, OrderNotificationService orderNotificationService) {
		this.orderService = orderService;
		this.orderNotificationService = orderNotificationService;
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping
	public ResponseEntity<EmitOrderServiceOutputDTO> emitOrder(
					@RequestBody @Valid EmitOrderInputDTO body
	) {
		try {
			return ResponseEntity.ok().body(this.orderService.emitOrder(body));
		} catch (InsufficientAssetsOnWallet e) {
			return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
		}
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping()
	public ResponseEntity<List<FetchOrdersOutputDTO.OrdersDTO>> fetchOrders(
	) {
		return ResponseEntity.ok().body(this.orderService.fetchOrders().getOrders());
	}

	@ApiResponse(content = {@Content(
					mediaType = "text/event-stream",
					schema = @Schema(implementation = OrderUpdatedEventDTO.class)
	)})
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/events")
	public SseEmitter streamChanges(
	) {
		return this.orderNotificationService.subscribe();
	}

}
