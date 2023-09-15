package dto

import "github.com/leo-the-nardo/internal/entity"

type OrderInput struct { //from kafka
	OrderID       string  `json:"order_id"`
	InvestorID    string  `json:"investor_id"`
	AssetID       string  `json:"asset_id"`
	CurrentShares int     `json:"current_shares"` //quantas ações que esse cara tem na carteira NO MOMENTO QUE ELE FEZ A ORDEM
	Shares        int     `json:"shares"`         //quantas ações ele quer comprar/vender
	Price         float64 `json:"price"`
	OrderType     string  `json:"order_type"` //buy/sell
}

func (this *OrderInput) ToEntity() (*entity.Order, []error) {
	return entity.NewOrder(
		this.OrderID,
		this.InvestorID,
		this.AssetID,
		this.Shares,
		this.Price,
		this.OrderType,
	)
}
