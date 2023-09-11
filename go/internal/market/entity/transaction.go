package entity

import "time"
import "github.com/google/uuid"

type Transaction struct { //match
	ID           string
	SellingOrder *Order
	BuyingOrder  *Order
	Shares       int
	Price        float64
	Total        float64
	DateTime     time.Time
}

func NewTransaction(sellingOrder *Order, buyingOrder *Order, shares int, price float64) *Transaction {
	return &Transaction{
		ID:           uuid.NewString(),
		SellingOrder: sellingOrder,
		BuyingOrder:  buyingOrder,
		Shares:       shares,
		Price:        price,
		Total:        float64(shares) * price,
		DateTime:     time.Now(),
	}
}

func (this *Transaction) CalculateTotal(shares int, price float64) {
	this.Total = float64(shares) * price
}

func (this *Transaction) CloseBuyOrder() {
	if this.BuyingOrder.PendingShares == 0 {
		this.BuyingOrder.Status = "CLOSED"
	}
}
func (this *Transaction) CloseSellOrder() {
	if this.SellingOrder.PendingShares == 0 {
		this.SellingOrder.Status = "CLOSED"
	}
}

func (this *Transaction) AddBuyOrderPendingShares(shares int) {
	this.BuyingOrder.PendingShares += shares
}
func (this *Transaction) AddSellOrderPendingShares(shares int) {
	this.SellingOrder.PendingShares += shares
}
