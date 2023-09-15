package dto

import "github.com/leo-the-nardo/internal/entity"

type OrderOutput struct {
	OrderID            string               `json:"order_id"`
	InvestorID         string               `json:"investor_id"`
	AssetID            string               `json:"asset_id"`
	OrderType          string               `json:"order_type"`
	Status             string               `json:"status"`
	Partial            int                  `json:"partial"`
	Shares             int                  `json:"shares"`
	TransactionsOutput []*TransactionOutput `json:"transactions"`
}

type TransactionOutput struct {
	TransactionID string  `json:"transaction_id"`
	BuyerID       string  `json:"buyer_id"`
	SellerID      string  `json:"seller_id"`
	AssetID       string  `json:"asset_id"`
	Price         float64 `json:"price"`
	Shares        int     `json:"shares"`
}

func NewOrderOutput(order *entity.Order) *OrderOutput {
	orderOutput := &OrderOutput{
		OrderID:    order.ID,
		InvestorID: order.InvestorID,
		AssetID:    order.AssetID,
		OrderType:  order.OrderType,
		Status:     order.Status,
		Partial:    order.PendingShares,
		Shares:     order.Shares,
	}
	for _, transaction := range order.Transactions {
		orderOutput.TransactionsOutput = append(orderOutput.TransactionsOutput, &TransactionOutput{
			TransactionID: transaction.ID,
			BuyerID:       transaction.BuyingOrder.InvestorID,
			SellerID:      transaction.SellingOrder.InvestorID,
			AssetID:       transaction.BuyingOrder.AssetID,
			Price:         transaction.Price,
			Shares:        transaction.Shares,
		})
	}
	return orderOutput
}
