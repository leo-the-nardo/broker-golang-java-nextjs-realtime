package dto

type TradeInput struct { //from kafka
	OrderID       string  `json:"order_id"`
	InvestorID    string  `json:"investor_id"`
	AssetID       string  `json:"asset_id"`
	CurrentShares int     `json:"current_shares"` //quantas ações que esse cara tem
	Shares        int     `json:"shares"`         //quantas ações ele quer comprar/vender
	Price         float64 `json:"price"`
	OrderType     string  `json:"order_type"` //buy/sell
}

type OrderOutput struct { //to kafka
	OrderID           string              `json:"order_id"`
	InvestorID        string              `json:"investor_id"`
	AssetID           string              `json:"asset_id"`
	OrderType         string              `json:"order_type"` //buy/sell
	Status            string              `json:"status"`     //open/closed
	Partial           int                 `json:"partial"`    //quanto ela ta de parcial
	Shares            int                 `json:"shares"`
	TransactionOutput []TransactionOutput `json:"transactions"`
}

type TransactionOutput struct {
	TransactionID string  `json:"transaction_id"`
	BuyerID       string  `json:"buyer_id"`
	SellerID      string  `json:"seller_id"`
	AssetID       string  `json:"asset_id"` //qual ação foi negociada
	Price         float64 `json:"price"`    //preço da ação
	Shares        int     `json:"shares"`   //quantas ações/cotas foram negociadas
}
