package com.leothenardo.homebroker.orders.dtos;

import com.leothenardo.homebroker.orders.entities.OrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EmitOrderInputDTO(
				@NotBlank String assetId,
				@NotNull OrderType type,
				@Positive int shares,
				@Positive Double price
) {
}
