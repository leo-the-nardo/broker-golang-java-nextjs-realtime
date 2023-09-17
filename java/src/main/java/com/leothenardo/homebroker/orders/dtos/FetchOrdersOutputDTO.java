package com.leothenardo.homebroker.orders.dtos;

import com.leothenardo.homebroker.orders.model.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FetchOrdersOutputDTO {
	private List<OrdersDTO> orders;

	public FetchOrdersOutputDTO() {
	}

	public static FetchOrdersOutputDTO from(List<Order> orderList) {
		FetchOrdersOutputDTO fetchOrdersOutputDTO = new FetchOrdersOutputDTO();
		List<OrdersDTO> ordersDTOList = new ArrayList<>();

		for (Order order : orderList) {
			OrdersDTO ordersDTO = new OrdersDTO();
			ordersDTO.id = order.getId();
			ordersDTO.type = order.getType().toString();
			ordersDTO.assetId = order.getAssetId();
			ordersDTO.price = order.getPrice();
			ordersDTO.shares = order.getShares();
			ordersDTO.status = order.getStatus().toString();
			ordersDTO.partial = order.getPartial();
			ordersDTO.createdAt = order.getCreatedAt();
			ordersDTO.updatedAt = order.getUpdatedAt();

			ordersDTOList.add(ordersDTO);
		}

		fetchOrdersOutputDTO.orders = ordersDTOList;
		return fetchOrdersOutputDTO;
	}

	public List<OrdersDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrdersDTO> orders) {
		this.orders = orders;
	}

	public static class OrdersDTO {
		private String id;
		private String type;
		private String assetId;
		private double price;
		private int shares;
		private String status;
		private int partial;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;

		public OrdersDTO() {
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getAssetId() {
			return assetId;
		}

		public void setAssetId(String assetId) {
			this.assetId = assetId;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public int getShares() {
			return shares;
		}

		public void setShares(int shares) {
			this.shares = shares;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public int getPartial() {
			return partial;
		}

		public void setPartial(int partial) {
			this.partial = partial;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}
	}
}
