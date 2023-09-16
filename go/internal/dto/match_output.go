package dto

import "github.com/leo-the-nardo/internal/entity"

type OrderOutput struct {
	OrderID            string               `json:"order_id"`
	InvestorID         string               `json:"investor_id"`
	OrderType          string               `json:"order_type"`
	Status             entity.OrderStatus   `json:"status"`
	Partial            int                  `json:"partial"`
	Price              float64              `json:"price"`
	Shares             int                  `json:"shares"`
	TransactionsOutput []*TransactionOutput `json:"transactions"`
}

type TransactionOutput struct {
	TransactionID string  `json:"transaction_id"`
	BuyerID       string  `json:"buyer_id"`
	SellerID      string  `json:"seller_id"`
	Price         float64 `json:"price"`
	Shares        int     `json:"shares"`
}

type MatchOutput struct {
	TransactionId string      `json:"transaction_id"`
	AssetId       string      `json:"asset_id"`
	Shares        int         `json:"shares"`
	Price         float64     `json:"price"`
	BuyOrder      OrderOutput `json:"buy_order"`
	SellOrder     OrderOutput `json:"sell_order"`
}

func NewMatchOutput(buyOrder *entity.Order, sellOrder *entity.Order, transaction *entity.Transaction) *MatchOutput {
	buyOrderOutput := newOrderOutput(buyOrder)
	sellOrderOutput := newOrderOutput(sellOrder)
	return &MatchOutput{
		BuyOrder:      *buyOrderOutput,
		SellOrder:     *sellOrderOutput,
		AssetId:       buyOrder.AssetID,
		Shares:        transaction.Shares,
		TransactionId: transaction.ID,
		Price:         transaction.Price,
	}
}

func newOrderOutput(order *entity.Order) *OrderOutput {
	orderOutput := &OrderOutput{
		OrderID:    order.ID,
		InvestorID: order.InvestorID,
		OrderType:  order.OrderType,
		Status:     order.Status,
		Partial:    order.PendingShares,
		Shares:     order.Shares,
		Price:      order.Price,
	}
	for _, transaction := range order.Transactions {
		orderOutput.TransactionsOutput = append(orderOutput.TransactionsOutput, &TransactionOutput{
			TransactionID: transaction.ID,
			BuyerID:       transaction.BuyingOrder.InvestorID,
			SellerID:      transaction.SellingOrder.InvestorID,
			Price:         transaction.Price,
			Shares:        transaction.Shares,
		})
	}
	return orderOutput
}
