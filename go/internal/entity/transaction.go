package entity

import (
	"errors"
	"time"
)
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

func NewTransaction(sellingOrder *Order, buyingOrder *Order) (*Transaction, []error) {
	price := sellingOrder.Price
	var sharesBeingTraded int
	if sellingOrder.PendingShares < buyingOrder.PendingShares {
		sharesBeingTraded = sellingOrder.PendingShares
	} else {
		sharesBeingTraded = buyingOrder.PendingShares
	}
	buyingOrder.PendingShares -= sharesBeingTraded
	sellingOrder.PendingShares -= sharesBeingTraded
	transaction := &Transaction{
		ID:           uuid.NewString(),
		SellingOrder: sellingOrder,
		BuyingOrder:  buyingOrder,
		Shares:       sharesBeingTraded,
		Price:        price,
		Total:        float64(sharesBeingTraded) * price,
		DateTime:     time.Now(),
	}
	foundErrors := transaction.validate()
	if len(foundErrors) > 0 {
		return nil, foundErrors
	}
	return transaction, nil
}

func (this *Transaction) validate() []error {
	var foundErrors []error

	if this.BuyingOrder.InvestorID == this.SellingOrder.InvestorID {
		foundErrors = append(foundErrors, errors.New("investorID cannot be the same"))
	}
	if this.BuyingOrder.AssetID != this.SellingOrder.AssetID {
		foundErrors = append(foundErrors, errors.New("assetID cannot be different"))
	}
	if this.SellingOrder.Price > this.BuyingOrder.Price {
		foundErrors = append(foundErrors, errors.New("sellingOrder price cannot be greater than buyingOrder price"))
	}
	if this.SellingOrder.OrderType != "SELL" {
		foundErrors = append(foundErrors, errors.New("sellingOrder must be of type SELL"))
	}
	if this.BuyingOrder.OrderType != "BUY" {
		foundErrors = append(foundErrors, errors.New("buyingOrder must be of type BUY"))
	}
	if this.SellingOrder.Status == "CLOSED" {
		foundErrors = append(foundErrors, errors.New("sellingOrder status cannot be CLOSED"))
	}
	if this.BuyingOrder.Status == "CLOSED" {
		foundErrors = append(foundErrors, errors.New("buyingOrder status cannot be CLOSED"))
	}
	if this.SellingOrder.PendingShares < 0 {
		foundErrors = append(foundErrors, errors.New("sellingOrder pendingShares must be greater than 0"))
	}
	if this.BuyingOrder.PendingShares < 0 {
		foundErrors = append(foundErrors, errors.New("buyingOrder pendingShares must be greater than 0"))
	}
	return foundErrors
}
